package com.gopai;

public class AvailableReportConfig {
    private String ReportGUID;
    private String ExternalName;

    public String getReportGUID() {
        return ReportGUID;
    }

    public void setReportGUID(String reportGUID) {
        ReportGUID = reportGUID;
    }

    public String getExternalName() {
        return ExternalName;
    }

    public void setExternalName(String externalName) {
        ExternalName = externalName;
    }

    @Override
    public String toString() {
        return "ReportConfig{" +
                "reportGuid='" + ReportGUID + '\'' +
                "      " +
                "ExternalName='" + ExternalName + '\'' +
                '}';
    }
}
