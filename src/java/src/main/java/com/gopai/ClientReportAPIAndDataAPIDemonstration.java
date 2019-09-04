package com.gopai;

import com.google.gson.Gson;

import java.io.*;

public class ClientReportAPIAndDataAPIDemonstration {
    public static void main(String[] args) throws Exception {
        PAIClient client = new PAIClient();
        client.connect("MyTestUser", "LetMeIn1");

        DemonstrateReportAPI(client);
        DemonstrateQueryAPISimpleSelectQuery(client);
        DemonstrateQueryAPISelectVisibleReports(client);
    }

    private static void DemonstrateReportAPI(PAIClient client) throws IOException {
        InputStream stream = client.retrieveReportUsingBuilder(RequestBuilder.report("GetNewUserReport").column("City").filter("Billings").visible(false).build());
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String consoleInput;

        while ((consoleInput = input.readLine()) != null)
            System.out.println(consoleInput);
        input.close();
    }

    private static void DemonstrateQueryAPISimpleSelectQuery(PAIClient client) throws IOException {
        InputStream stream = client.runQuery("SELECT * FROM Users u WHERE UserName = 'MyTestUser'");
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));

        String consoleInput;

        while ((consoleInput = input.readLine()) != null)
            System.out.println(consoleInput);

        input.close();
    }

    private static void DemonstrateQueryAPISelectVisibleReports(PAIClient client) throws IOException {
        InputStream stream = client.runQuery("SELECT TOP 10 * FROM ReportConfigs s");
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


