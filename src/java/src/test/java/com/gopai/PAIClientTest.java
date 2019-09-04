package com.gopai;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PAIClientTest {
    @Test
    public void name() throws IOException {
        PAIClient paiClient = new PAIClient();
        paiClient.connect("a", "b");
        System.out.println(paiClient.retrieveReportConfig("43377A74-92B8-E911-80CF-001B213793BD"));
    }

    @Test
    public void queryTest() throws IOException {
        PAIClient paiClient = new PAIClient();
        paiClient.connect("a", "b");
        InputStream stream = paiClient.runQuery("SELECT TOP 1 r.ReportGUID, r.ExternalName FROM ReportConfigs r WHERE r.ExternalName = 'softwarepackages'");
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String consoleInput;

        while ((consoleInput = input.readLine()) != null)
            System.out.println(consoleInput);
        input.close();
    }
}