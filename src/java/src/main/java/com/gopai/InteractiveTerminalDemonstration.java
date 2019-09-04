package com.gopai;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class InteractiveTerminalDemonstration {

    public static void main(String[] args) throws Exception {
        startTerminalDemonstration();
    }

    public static void startTerminalDemonstration() throws IOException {
        String inputText = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        PAIClient client = startClientConnectionDialog(reader);

        while (client != null) {

            inputText = startDemoSelectionDialog(reader);

            if (inputText.equalsIgnoreCase("quit"))
                break;
            else if (inputText.toUpperCase().charAt(0) == 'Q')
                startRunQueryDialog(reader, client);
            else if (inputText.toUpperCase().charAt(0) == 'R')
                startRunReportDialog(reader, client);
        }
        System.out.println("Thanks for playing.");
    }

    public static PAIClient startClientConnectionDialog(BufferedReader reader) throws IOException {
        PAIClient client = new PAIClient();
        while (!client.isConnected()) {
            System.out.println("Please enter a User :");
            String username = reader.readLine();
            System.out.println("Please enter the Password for User :");
            String password = reader.readLine();
            client.connect(username, password);
            if (!client.isConnected()) {
                if (!startYesNoDialog(reader, "Could not connect. Would you like to try again? Y(es) or N(o) :"))
                    return null;
            }
        }
        return client;
    }

    public static String startDemoSelectionDialog(BufferedReader reader) throws IOException {
        List<String> validInputs = Arrays.asList("QUERY", "Q", "REPORT", "R", "QUIT");
        String inputText = "";
        while (!validInputs.contains(inputText.toUpperCase())) {
            System.out.println("Enter Q(uery), R(eport), or Quit");
            inputText = reader.readLine();
        }
        return inputText;
    }

    public static void startRunQueryDialog(BufferedReader reader, PAIClient client) throws IOException {
        System.out.println("You may write simple single line sql queries with the table aliased or enter quit to exit :");
        String inputText = "";
        while (!inputText.equalsIgnoreCase("quit")) {
            inputText = reader.readLine();
            if (!inputText.equalsIgnoreCase("quit")) {
                runQuery(client, inputText);
            }
        }
    }

    public static void runQuery(PAIClient client, String inputText) {
        try {
            InputStream stream = client.runQuery(inputText);
            printResultsToScreen(stream);
        } catch (IOException e) {
            System.out.println("The query failed for some reason. Try another query or enter Quit.");
        }
    }

    public static void startRunReportDialog(BufferedReader reader, PAIClient client) throws IOException {
        startVisibleReportsDialog(reader, client);

        AvailableReportConfig reportConfig = startReportSelectionDialog(reader, client);

        if (reportConfig != null) {
            RequestBuilder builder = RequestBuilder.report((reportConfig.getExternalName()));
            builder.getRequest().setReportGUID(reportConfig.getReportGUID());

            startAddColumnsDialog(reader, client, builder);

            try {
                InputStream stream = client.retrieveReportUsingBuilder(builder);
                if (startPrintOrSaveDialog(reader, "Would you like to V(iew) or S(ave) the results?") == 'S')
                    client.saveReportToFile(builder.request.reportName, stream);
                else
                    printResultsToScreen(stream);
            } catch (RuntimeException e) {
                System.out.println("The report failed. Try again later.");
            }
        }
    }

    public static void startVisibleReportsDialog(BufferedReader reader, PAIClient client) throws IOException {
        if (startYesNoDialog(reader, "Would you like to see a list of reports? Y(es) or N(o):")) {

            InputStream stream = client.runQuery("SELECT * FROM ReportConfigs r ORDER BY r.ExternalName");

            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            StringBuilder text = new StringBuilder();
            String consoleInput;
            while ((consoleInput = input.readLine()) != null)
                text.append(consoleInput);
            input.close();

            AvailableReportConfig[] configs = new Gson().fromJson(text.toString(), AvailableReportConfig[].class);

            for (AvailableReportConfig config : configs) {
                System.out.println(config);
            }
        }
    }

    public static AvailableReportConfig startReportSelectionDialog(BufferedReader reader, PAIClient client) throws IOException {
        AvailableReportConfig reportConfig = null;
        String consoleInput = "";
        System.out.println("Please enter the ExternalName of the report you want to run: ");
        consoleInput = reader.readLine();
        while (reportConfig == null && !consoleInput.equalsIgnoreCase("quit")) {

            InputStream stream = client.runQuery("SELECT TOP 1 r.ReportGUID, r.ExternalName FROM ReportConfigs r WHERE r.ExternalName = '" + consoleInput + "'");
            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            String returnValue = input.readLine();
            input.close();

            AvailableReportConfig[] reportConfigs = new Gson().fromJson(returnValue, AvailableReportConfig[].class);

            if (reportConfigs.length == 0) {
                System.out.println(consoleInput + " either doesn't exist or isn't accessible. Please try again or enter Quit :");
                consoleInput = reader.readLine();
            } else
                reportConfig = reportConfigs[0];
        }
        return reportConfig;
    }

    public static void startAddColumnsDialog(BufferedReader reader, PAIClient client, RequestBuilder builder) throws IOException {
        while (startYesNoDialog(reader, "Would you like to add additional filters? Y(es) or N(o)")) {

            ReportConfig config = client.retrieveReportConfig(builder.getRequest().getReportGUID());

            System.out.println("Here are a list of columns in that report.");
            for (ReportConfig.Field field : config.getFields())
                System.out.println(field.name);

            System.out.println("Please enter a column name (Column names are case sensitive) :");
            String columnName = reader.readLine();

            for (ReportConfig.Field field : config.getFields()) {
                if (field.name.equals(columnName)) {
                    System.out.println("Label: Value");
                    if (field.data.getValues() != null)
                        for (ReportConfig.Field.Select value : field.data.getValues())
                            System.out.println(value.getLabel() + ": " + value.getKey());
                }
            }
            System.out.println("Please enter a filter value :");
            String filterVal = reader.readLine();

            System.out.println("Please enter if it should be visible (Y or N) :");
            String visible = reader.readLine();

            builder.column(columnName)
                    .filter(filterVal)
                    .visible(visible.equalsIgnoreCase("y"))
                    .build();
        }
    }

    private static char startPrintOrSaveDialog(BufferedReader reader, String message) throws IOException {
        System.out.println(message);
        String inputText = reader.readLine();
        List<String> validInputs = Arrays.asList("VIEW", "V", "SAVE", "S");
        while (!validInputs.contains(inputText.toUpperCase())) {
            System.out.println("Invalid input. Please enter V(iew) or S(ave) :");
            inputText = reader.readLine();
        }
        return inputText.charAt(0);
    }

    private static boolean startYesNoDialog(BufferedReader reader, String message) throws IOException {
        System.out.println(message);
        String inputText = reader.readLine();
        List<String> validInputs = Arrays.asList("YES", "Y", "NO", "N");
        while (!validInputs.contains(inputText.toUpperCase())) {
            System.out.println("Invalid input. Please enter Y(es) or N(o) :");
            inputText = reader.readLine();
        }
        return inputText.toUpperCase().charAt(0) == 'Y';
    }

    private static void printResultsToScreen(InputStream stream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String consoleInput;

        while ((consoleInput = input.readLine()) != null)
            System.out.println(consoleInput);
        input.close();
    }
}
