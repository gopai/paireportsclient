package com.gopai.cli;

import com.gopai.pair.sdk.v1.ReportConfig;
import com.gopai.data.SelectableReportConfigs;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ReportFilterTerminal {

    SelectableReportConfigs reportColumns;
    BufferedReader reader;

    public ReportFilterTerminal(ReportConfig config, BufferedReader reader) throws IOException {
        this.reportColumns = new SelectableReportConfigs(config);
        this.reader = reader;
    }

    public void startAddFiltersDialog() throws IOException {
        while (TerminalUtils.startYesNoDialog(reader, "Would you like to add additional filters? Y(es) or N(o)")) {
            printFieldHeaders();
            printFields();
            int fieldInd = getIntFromUser(reportColumns.getConfig().getFields().size());
            if (doesFieldHaveValues(fieldInd)) {
                printValueHeaders();
                printValues(fieldInd);
                reportColumns.selectByIndex(fieldInd, getIntFromUser(reportColumns.getConfig().getField(fieldInd).getData().getValues().size()), getVisibilityFromUser());
            }
            else{
                System.out.println("Please enter a filter value :");
                reportColumns.selectByString(fieldInd, reader.readLine(), getVisibilityFromUser());
            }
        }
    }

    private int getIntFromUser(int ceiling) throws IOException {
        int returnInt = -1;
        while (returnInt < 0 || returnInt > ceiling)
            try {
                System.out.println("Please enter the index selection :");
                returnInt = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) { }
        return returnInt;
    }

    private boolean getVisibilityFromUser() throws IOException {
        return TerminalUtils.startYesNoDialog(reader, "Enter if it should be visible Y(es) or N(o) :");
    }

    private void printFieldHeaders() {
        System.out.printf("%-5s | %-40.40s |%n", "Index", "Column Name");
        for (int i = 0; i < 114; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

    private void printFields() {
        System.out.println("Here are a list of columns in that report.");
        for (int i = 0; i < reportColumns.getConfig().getFields().size(); i++) {
            System.out.printf("%-5s | %-40.40s |%n", i, reportColumns.getConfig().getField(i).getName());
        }
    }

    private void printValueHeaders() {
        System.out.printf("%-5s | %-40.40s |%n", "Index", "Filter Name");
        for (int i = 0; i < 114; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

    private void printValues(int fieldIndex) {
        List<ReportConfig.Field.Value> values = reportColumns.getConfig().getField(fieldIndex).getData().getValues();
        for (int i = 0; i < values.size(); i++) {
            System.out.printf("%-5d | %-40.40s |%n", i, values.get(i).getLabel());
        }
    }

    boolean doesFieldHaveValues(int fieldInd) {
        return reportColumns.getConfig().getField(fieldInd).getData() != null
                && reportColumns.getConfig().getField(fieldInd).getData().getValues() != null
                && reportColumns.getConfig().getField(fieldInd).getData().getValues().size() > 0;
    }

    public SelectableReportConfigs getReportColumns() {
        return reportColumns;
    }

    public void setReportColumns(SelectableReportConfigs reportColumns) {
        this.reportColumns = reportColumns;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }
}
