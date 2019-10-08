package com.gopai.cli;

import com.gopai.pair.sdk.v1.ReportConfig;
import com.gopai.data.ReportIdentifier;
import com.gopai.data.SelectableReportConfigs;
import com.gopai.pair.sdk.v1.PAIClient;
import com.gopai.pair.sdk.v1.ReportRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ReportTerminal {

    BufferedReader reader;
    PAIClient client;
    ReportIdentifierTerminal identifierTerminal;
    ReportFilterTerminal filterTerminal;

    public ReportTerminal(BufferedReader reader, PAIClient client) {
        this.reader = reader;
        this.client = client;
    }

    public void startReportDialog() throws IOException {
        ReportIdentifier reportID = getReportIdentifier();
        if (reportID == null) return;
        SelectableReportConfigs columns = getReportFilters(reportID);
        ReportRequest reportRequest = createRequestBuilder(reportID, columns);
        try {
            InputStream stream = client.retrieveReportUsingBuilder(reportRequest);
            if (startPrintOrSaveDialog(reader, "Would you like to V(iew) or S(ave) the results?").equalsIgnoreCase("S"))
                TerminalUtils.saveReportToFile(reportRequest.getReportName(), TerminalUtils.startDirectorySelectionDialog(reader), stream);
            else
                TerminalUtils.printResultsToScreen(stream);
        } catch (RuntimeException e) {
            System.out.println("The report failed. Try again later.");
        }
    }

    private ReportIdentifier getReportIdentifier() throws IOException {
        identifierTerminal = new ReportIdentifierTerminal(reader, client);
        identifierTerminal.userSelectSingleOption();
        return identifierTerminal.identifiers.getSelectedOptions().get(0);
    }

    private SelectableReportConfigs getReportFilters(ReportIdentifier reportID) throws IOException {
        ReportConfig config = client.retrieveReportConfig(reportID.getReportGUID());
        filterTerminal = new ReportFilterTerminal(config, reader);
        filterTerminal.startAddFiltersDialog();
        return filterTerminal.getReportColumns();
    }

    ReportRequest createRequestBuilder(ReportIdentifier reportID, SelectableReportConfigs columns) {
        ReportRequest reportRequest = ReportRequest.report((reportID.getExternalName()));
        reportRequest.setReportGUID(reportID.getReportGUID());
        reportRequest.setReportName(reportID.getName());
        for (ReportConfig.Field field : columns.getReturnReport().getFields()) {
            reportRequest
                    .column(field.getName())
                    .setFilter(field.getData().getValue(0).getKey())
                    .setVisible(field.isReadonly())
                    .build();
        }
        return reportRequest;
    }

    private String startPrintOrSaveDialog(BufferedReader reader, String message) throws IOException {
        System.out.println(message);
        String inputText = reader.readLine();
        List<String> validInputs = Arrays.asList("VIEW", "V", "SAVE", "S");
        while (!validInputs.contains(inputText.toUpperCase())) {
            System.out.println("Invalid input. Please enter V(iew) or S(ave) :");
            inputText = reader.readLine();
        }
        return inputText;
    }
}
