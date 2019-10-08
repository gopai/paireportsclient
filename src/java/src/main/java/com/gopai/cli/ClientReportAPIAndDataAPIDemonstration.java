package com.gopai.cli;

import com.google.gson.Gson;
import com.gopai.pair.sdk.v1.PAIClient;
import com.gopai.pair.sdk.v1.ReportRequest;
import com.gopai.data.ReportIdentifier;

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
        InputStream stream = client.retrieveReportUsingBuilder(ReportRequest.report("GetNewUserReport").column("City").setFilter("Billings").setVisible(false).build());
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

        ReportIdentifier[] configs = new Gson().fromJson(text.toString(), ReportIdentifier[].class);

        for (ReportIdentifier config : configs) {
            System.out.println(config);
        }
    }
}


