package com.gopai.data;

import com.gopai.pair.sdk.v1.ReportConfig;

import java.util.ArrayList;

public class SelectableReportConfigs {
    ReportConfig config;
    ReportConfig returnReport;

    public SelectableReportConfigs(ReportConfig config) {
        this.config = config;
        initiateReturnConfig();
    }

    public void initiateReturnConfig() {
        returnReport = new ReportConfig();
        returnReport.setFields(new ArrayList<>());
    }

    private ReportConfig.Field selectFieldFromIndex(int fieldInd, int valueInd, boolean isVisible) {
        ReportConfig.Field field = createField(fieldInd, isVisible);
        ArrayList values = new ArrayList<>();
        values.add(config.getField(fieldInd).getData().getValue(valueInd));
        field.getData().setValues(values);
        return field;
    }

    private ReportConfig.Field selectFieldFromString(int fieldInd, String filterValue, boolean isVisible) {
        ReportConfig.Field field = createField(fieldInd, isVisible);
        ArrayList values = new ArrayList<>();
        ReportConfig.Field.Value value = new ReportConfig.Field.Value();
        value.setKey(filterValue);
        value.setLabel(filterValue);
        values.add(value);
        field.getData().setValues(values);
        return field;
    }

    private ReportConfig.Field createField(int fieldInd, boolean isVisible) {
        ReportConfig.Field field = new ReportConfig.Field();
        field.setName(config.getField(fieldInd).getName());
        field.setType(config.getField(fieldInd).getType());
        field.setReadonly(isVisible);
        ReportConfig.Field.Data data = new ReportConfig.Field.Data();
        data.setForced(config.getField(fieldInd).getData().isForced());
        data.setType(config.getField(fieldInd).getData().getType());
        field.setData(data);
        return field;
    }

    private ArrayList createValues(String filterValue){
        ArrayList values = new ArrayList();
        ReportConfig.Field.Value value = new ReportConfig.Field.Value();
        value.setKey(filterValue);
        value.setLabel(filterValue);
        values.add(value);
        return values;
    }

    public void selectByIndex(int fieldInd, int valueInd, boolean isVisible) {
        if (fieldInd < config.getFields().size() && fieldInd > -1)
            if (valueInd < config.getField(fieldInd).getData().getValues().size() && valueInd > -1)
                returnReport.getFields().add(selectFieldFromIndex(fieldInd, valueInd, isVisible));
    }

    public void selectByIndex(int fieldInd, int valueInd) {
        selectByIndex(fieldInd, valueInd, true);
    }

    public void selectByString(int fieldInd, String filterValue, boolean isVisible) {
        if (fieldInd < config.getFields().size() && fieldInd > -1)
            returnReport.getFields().add(selectFieldFromString(fieldInd, filterValue, isVisible));
    }

    public void selectByString(int fieldInd, String filterValue) {
        selectByString(fieldInd, filterValue, true);
    }

    public ReportConfig getReturnReport() {
        return returnReport;
    }

    public ReportConfig getConfig() {
        return config;
    }

    public void setConfig(ReportConfig config) {
        this.config = config;
    }
}
