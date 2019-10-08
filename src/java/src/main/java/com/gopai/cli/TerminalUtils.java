package com.gopai.cli;

import com.gopai.pair.sdk.v1.PAIClient;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TerminalUtils {

    public static String user = "YourUserHere";
    public static String pass = "YourPasswordHere";

    public static PAIClient getTestClient() throws IOException {
        PAIClient client = new PAIClient();
        client.connect(user, pass);
        return client;
    }

    public static BufferedReader getDefaultReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    public static PAIClient startClientConnectionDialog(BufferedReader reader, PAIClient client) throws IOException {
        Console console = System.console();
        while (!client.isConnected()) {
            System.out.println("Please enter a User :");
            String username = console.readLine();
            System.out.println("Please enter the Password for User :");
            String password = new String(console.readPassword());
            if (!client.connect(username, password)) {
                if (!TerminalUtils.startYesNoDialog(reader, "Could not connect. Would you like to try again? Y(es) or N(o) :"))
                    return null;
            }
        }
        return client;
    }

    public static boolean startYesNoDialog(BufferedReader reader, String message) throws IOException {
        System.out.println(message);
        String inputText = reader.readLine();
        List<String> validInputs = Arrays.asList("YES", "Y", "NO", "N");
        while (!validInputs.contains(inputText.toUpperCase())) {
            System.out.println("Invalid input. Please enter Y(es) or N(o) :");
            inputText = reader.readLine();
        }
        return inputText.toUpperCase().charAt(0) == 'Y';
    }

    public static void printResultsToScreen(InputStream stream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String consoleInput;
        while ((consoleInput = input.readLine()) != null)
            System.out.println(consoleInput);
        input.close();
    }

    public static String startDirectorySelectionDialog(BufferedReader reader) throws IOException {
        System.out.println("Please enter the Directory path you would like to save to :");
        File dir = new File("");
        while (!dir.exists()) {
            dir = new File(reader.readLine());
            if (!dir.exists())
                System.out.println("That Directory doesn't exist. Please try again :");
        }
        return dir.getCanonicalPath();
    }

    public static void saveReportToFile(String name, String path, InputStream stream) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
            File file = new File(path + "\\"+ name + "_" + LocalDateTime.now().format(formatter) + ".csv");
            java.nio.file.Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            stream.close();
            System.out.println("Saved as " + file.getName() + " in " + file.getPath());
        } catch (IOException e) {
            System.out.println("Could not save file.");
        }
    }
}
