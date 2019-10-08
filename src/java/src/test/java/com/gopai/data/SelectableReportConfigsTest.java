package com.gopai.data;

import com.gopai.pair.sdk.v1.ReportConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SelectableReportConfigsTest {

    SelectableReportConfigs config;

    @Before
    public void setUp() throws Exception {
        ReportConfig reportConfig = makeRandomReportConfig("TestField", 3, 3);
        config = new SelectableReportConfigs(reportConfig);
    }

    static ReportConfig makeRandomReportConfig(String name, int numberOfFields, int numberOfValues) {
        ReportConfig reportConfig = new ReportConfig();
        List<ReportConfig.Field> fields = new ArrayList<>();
        for (int i = 0; i < numberOfFields; i++) {
            fields.add(makeRandomConfigField(name + i, numberOfValues));
        }
        reportConfig.setFields(fields);
        return reportConfig;
    }

    static ReportConfig.Field makeRandomConfigField(String name, int numberOfValues) {
        ReportConfig.Field field = new ReportConfig.Field();
        field.setName(name);
        field.setType(name + "_Type");
        field.setReadonly(true);
        field.setData(makeRandomFieldData(name + "_TestData", numberOfValues));
        return field;
    }

    static ReportConfig.Field.Data makeRandomFieldData(String name, int numberOfValues) {
        ReportConfig.Field.Data data = new ReportConfig.Field.Data();
        List<ReportConfig.Field.Value> values = new ArrayList<>();
        for (int i = 0; i < numberOfValues; i++) {
            values.add(makeRandomFieldValue(name + i + "_"));
        }
        data.setValues(values);
        return data;
    }

    static ReportConfig.Field.Value makeRandomFieldValue(String name) {
        ReportConfig.Field.Value value = new ReportConfig.Field.Value();
        value.setKey(name + "key");
        value.setLabel(name + "label");
        return value;
    }

    @Test
    public void randomReportConfigTest() {
        assertThat(config.getConfig().getFields().size(), is(3));
    }

    @Test
    public void given_ReportConfigWithNoValues_Expect_nothing() {
        config.setConfig(makeRandomReportConfig("EmptyConfig", 0, 0));
        assertThat(config.getConfig().getFields().size(), is(0));
    }

    @Test
    public void given_SelectFieldAndValue_Expect_ListsOfSizeOne() {
        config.setConfig(makeRandomReportConfig("2Items", 2, 2));
        config.selectByIndex(1, 1);
        assertThat(config.getReturnReport().getFields().size(), is(1));
    }

    @Test
    public void given_SelectFromEmptyConfig_Expect_Nothing() {
        config.setConfig(makeRandomReportConfig("EmptyConfig", 0, 0));
        config.selectByIndex(1, 1);
        assertThat(config.getReturnReport().getFields().size(), is(0));
    }

    @Test
    public void given_Select6ValuesFromIndex_ExpectConfigWith6Fields() {
        config.setConfig(makeRandomReportConfig("5Items", 5, 5));
        config.selectByIndex(1,1);
        config.selectByIndex(1,2);
        config.selectByIndex(1,3);
        config.selectByIndex(4,1);
        config.selectByIndex(4,2);
        config.selectByIndex(4,3);
        assertThat(config.getReturnReport().getFields().size(), is(6));
    }

    @Test
    public void given_Select6ValuesFromStrings_ExpectConfigWith6Fields() {
        config.setConfig(makeRandomReportConfig("5Items", 5, 0));
        config.selectByString(1,"Value 1");
        config.selectByString(1,"Value 2");
        config.selectByString(1,"Value 3");
        config.selectByString(4,"Value 4");
        config.selectByString(4,"Value 5");
        config.selectByString(4,"Value 6");
        assertThat(config.getReturnReport().getFields().size(), is(6));
    }


}