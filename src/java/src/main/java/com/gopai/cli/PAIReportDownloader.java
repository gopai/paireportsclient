package com.gopai.cli;

import com.google.gson.Gson;
import com.gopai.pair.sdk.v1.PAIClient;
import com.gopai.pair.sdk.v1.StreamUtil;
import com.gopai.data.ReportIdentifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PAIReportDownloader {

    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);

        if (arguments.missingArguments()) {
            System.err.println("The Downloader can only accept arguments of the form\n" +
                    "\"-u User -p Password -guid ReportGUID -path DesiredDirectory\"\n" +
                    "or to send it to the default output\n" +
                    "\"-u User -p Password -guid ReportGUID -o\"\n" +
                    "\nAlternatively to start terminal application you can enter\n" +
                    "\"-i\" or \"-u User -p Password -i\"\n" +
                    "\nPlease try again.");
            System.exit(1);
        }

        try (PAIClient client = new PAIClient()) {
            if (arguments.hasLoginCredentials()) {
                if (!client.connect(arguments.getUsername(), arguments.getPassword())) {
                    System.err.println("Could not connect with provided credentials. Please double check and try again.");
                    System.exit(10);
                }
            }
            if (arguments.getInteractiveMode()) {
                if (client.isConnected())
                    MasterTerminal.startTerminal(client);
                else
                    MasterTerminal.startTerminalRequireLogin(client);
            } else {
                ReportIdentifier reportConfig = GetSelectedGUID(client, arguments.getGuid());
                if (reportConfig == null) {
                    System.err.println("Either could not find GUID: " + arguments.getGuid() + " or provided User does not have access. Please try again.");
                    System.exit(20);
                }

                if (arguments.getSendToOut())
                    StreamUtil.copy(client.retrieveReportUsingGUID(reportConfig.getReportGUID()), System.out);
                else
                    TerminalUtils.saveReportToFile(reportConfig.getExternalName(), arguments.getPath(), client.retrieveReportUsingGUID(reportConfig.getReportGUID()));
            }
        }
        ;
    }


    private static ReportIdentifier GetSelectedGUID(PAIClient client, String guid) throws IOException {
        InputStream stream = client.runQuery("SELECT TOP 1 r.ReportGUID, r.ExternalName FROM ReportConfigs r WHERE r.ReportGUID = '" + guid + "'");
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String returnValue = input.readLine();
        input.close();

        ReportIdentifier[] reportConfigs = new Gson().fromJson(returnValue, ReportIdentifier[].class);

        if (reportConfigs.length > 0)
            return reportConfigs[0];
        else
            return null;
    }

}
