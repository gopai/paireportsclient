package com.gopai;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PAIReportDownloader {

    public static void main(String[] args) throws Exception {

        int userInd = -1;
        int passInd = -1;
        int GUIDInd = -1;
        int pathInd = -1;

        for (int i = 0; i < args.length; i++){
            if (args[i].equalsIgnoreCase("-u"))
                userInd = i + 1;
            else if (args[i].equalsIgnoreCase("-p"))
                passInd = i + 1;
            else if (args[i].equalsIgnoreCase("-guid"))
                GUIDInd = i + 1;
            else if (args[i].equalsIgnoreCase("-path"))
                pathInd = i + 1;
        }

        if (userInd< 0 || passInd < 0 || GUIDInd < 0 || pathInd < 0){
            System.out.println("The Downloader can only accept arguments of the form -u User -p Password -guid ReportGUID -path DesiredDirectory");
            System.out.println("Please try again.");
            return;
        }

        PAIClient client = new PAIClient();

        if (!client.connect(args[userInd], args[passInd])){
            System.out.println("Could not connect with provided credentials. Please double check and try again.");
            return;
        }

        AvailableReportConfig reportConfig = GetSelectedGUID(client, args[GUIDInd]);
        if(reportConfig == null){
            System.out.println("Either could not find GUID or provided User does not have access. Please try again.");
            return;
        }

        client.saveReportToFile(reportConfig.getExternalName(), args[pathInd], client.retrieveReportUsingGUID(args[GUIDInd]));
    }

    private static AvailableReportConfig GetSelectedGUID(PAIClient client, String guid) throws IOException {
        InputStream stream = client.runQuery("SELECT TOP 1 r.ReportGUID, r.ExternalName FROM ReportConfigs r WHERE r.ReportGUID = '" + guid + "'");
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String returnValue = input.readLine();
        input.close();

        AvailableReportConfig[] reportConfigs = new Gson().fromJson(returnValue, AvailableReportConfig[].class);

        if (reportConfigs.length > 0)
            return reportConfigs[0];
        else
            return null;
    }
}
