package com.gopai;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class RequestBuilder {

    Request request = new Request();

    public RequestBuilder() {
    }

    public ColumnBuilder column(String name) {
        return new ColumnBuilder(this).name(name);
    }

    public Request build() {
        return request;
    }

    Request getRequest() {
        return request;
    }

    public static RequestBuilder report(String reportName) {
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.getRequest().setReportName(reportName);
        return requestBuilder;
    }

    public class Request {

        List<ColumnBuilder.Column> columns = new ArrayList<>();
        String reportName;

        String reportGUID;

        public List<ColumnBuilder.Column> getColumns() {
            return columns;
        }

        public void setColumns(List<ColumnBuilder.Column> columns) {
            this.columns = columns;
        }

        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public String getReportGUID() {
            return reportGUID;
        }

        public void setReportGUID(String reportGUID) {
            this.reportGUID = reportGUID;
        }

        @Override
        public String toString() {
            for (ColumnBuilder.Column column : columns) {
            }
            return columns.stream()
                    .map((column) -> "F_" + column.name + "=" + column.filter + "&ED_" + column.name + "=" + column.isVisible)
                    .collect(joining("&"));
        }
    }
}
