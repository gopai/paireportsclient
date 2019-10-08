package com.gopai.pair.sdk.v1;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * The {@link ReportRequest} that contains the Report values and filter {@link Column}s for use by the {@link PAIClient}
 */
public class ReportRequest {
    private List<Column> columns = new ArrayList<>();
    private String reportName;
    private String reportGUID;

    /**
     * The static constructor for the {@link ReportRequest}.
     *
     * @param reportName
     * @return {@link ReportRequest}
     */
    public static ReportRequest report(String reportName) {
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setReportName(reportName);
        return reportRequest;
    }

    /**
     * @param reportName
     * @return {@link ReportRequest}
     */
    public ReportRequest setReportName(String reportName) {
        this.reportName = reportName;
        return this;
    }

    /**
     * @return ReportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @param reportGUID
     * @return {@link ReportRequest}
     */
    public ReportRequest setReportGUID(String reportGUID) {
        this.reportGUID = reportGUID;
        return this;
    }

    /**
     * @return ReportGUID
     */
    public String getReportGUID() {
        return reportGUID;
    }

    /**
     * Start point for the column building process to add filters to Report Requests.
     *
     * @param columnName
     * @return {@link Column}
     */
    public Column column(String columnName) {
        return new Column(this).setName(columnName);
    }

    /**
     * @param columns
     * @return {@link ReportRequest}
     */
    public ReportRequest setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * @return List of {@link Column}s
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * The build step to get the finished {@link ReportRequest}
     *
     * @return {@link ReportRequest}
     */
    public ReportRequest build() {
        return this;
    }

    /**
     * Returns a string with the formatted filter {@link Column}s for the {@link ReportRequest}.
     *
     * @return Formatted Columns String
     */
    @Override
    public String toString() {
        return columns.stream()
                .map((column) -> "F_" + column.getName() + "=" + column.getFilter() + "&ED_" + column.getName() + "=" + column.getVisible())
                .collect(joining("&"));
    }

    /**
     * The filter column which contains information for use by the {@link ReportRequest}.
     */
    public static class Column {
        ReportRequest rBuilder;

        private String name;
        private String filter;
        private boolean visibility = true;

        public Column() {
        }

        /**
         * The constructor and start point for the filter column building process.
         *
         * @param builder
         */
        public Column(ReportRequest builder) {
            this.rBuilder = builder;
        }

        /**
         * @param name
         * @return {@link Column}
         */
        public Column setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * @param filter
         * @return {@link Column}
         */
        public Column setFilter(String filter) {
            this.filter = filter;
            return this;
        }

        /**
         * @return filter
         */
        public String getFilter() {
            return filter;
        }

        /**
         * @param visibility
         * @return {@link Column}
         */
        public Column setVisible(boolean visibility) {
            this.visibility = visibility;
            return this;
        }

        /**
         * @return visibility
         */
        public boolean getVisible() {
            return visibility;
        }

        /**
         * The final step to get the finished column and add it to the originally provided {@link ReportRequest}.
         *
         * @return {@link ReportRequest}
         */
        public ReportRequest build() {
            rBuilder.getColumns().add(this);
            return rBuilder;
        }

        /**
         * Build a single stand alone {@link Column}.
         *
         * @return {@link Column}
         */
        public Column buildColumn() {
            return this;
        }
    }

}
