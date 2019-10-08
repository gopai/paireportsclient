package com.gopai.pair.sdk.v1;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReportRequestTest {

    @Test
    public void given_NameAndGUID_ExpectSameBackWithEmptyList() {
        ReportRequest request = new ReportRequest();
        request.setReportName("Name").setReportGUID("GUID").build();
        assertThat(request.getReportName(), is("Name"));
        assertThat(request.getReportGUID(), is("GUID"));
        assertThat(request.getColumns().size(), is(0));
    }

    @Test
    public void given_singlecolumn_with_filter_expect_keyvalue() {
        ReportRequest reportRequest = new ReportRequest()
                .column("Username")
                .setName("Username")
                .setFilter("name").build()
                .build();
        assertThat(reportRequest.toString(), is("F_Username=name&ED_Username=true"));
    }

    @Test
    public void given_multiplecolumn_with_filter_expect_keyvalue() {
        ReportRequest reportRequest = new ReportRequest()
                .column("Username")
                .setName("Username")
                .setFilter("name")
                .build()
                .column("Business")
                .setName("Business")
                .setFilter("business")
                .build()
                .column("Cust#")
                .setName("Cust#")
                .setFilter("cust#")
                .build()
                .column("Type")
                .setName("Type")
                .setFilter("type")
                .build()
                .column("Name")
                .setName("Name")
                .setFilter("name")
                .build()
                .column("Department")
                .setName("Department")
                .setFilter("department")
                .build()
                .build();
        assertThat(reportRequest.toString(), is("F_Username=name&ED_Username=true&F_Business=business&ED_Business=true&F_Cust#=cust#&ED_Cust#=true&F_Type=type&ED_Type=true&F_Name=name&ED_Name=true&F_Department=department&ED_Department=true"));
    }

}