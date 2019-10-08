package com.gopai.data;

public class ReportIdentifier {
    private String ReportGUID;
    private String ExternalName;
    private String Name;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "AvailableReportConfig{" +
                "ReportGUID='" + ReportGUID + '\'' +
                ", ExternalName='" + ExternalName + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}
