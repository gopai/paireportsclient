package com.gopai;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created by jemima.nyamogo on 5/25/2017.
 */
public class Request {

    List<Column> columns = new ArrayList<>();
    String reportName;

    public List<Column> getColumns() {

        return columns;
    }

    public void setColumns(List<Column> columns) {

        this.columns = columns;
    }

    public String getReportName() {

        return reportName;
    }

    public void setReportName(String reportName) {

        this.reportName = reportName;
    }

    @Override
    public String toString() {

        for (Column column : columns) {

        }
        return columns.stream()
                .map((column) -> "F_" + column.name + "=" + column.filter + "&ED_" + column.name + "=" + column.isVisible)
                .collect(joining("&"));
    }
}
