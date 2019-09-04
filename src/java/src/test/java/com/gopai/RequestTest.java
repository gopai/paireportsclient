package com.gopai;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void given_singlecolumn_with_filter_expect_keyvalue() {
        RequestBuilder rBuilder = new RequestBuilder();
        RequestBuilder.Request request = rBuilder
                .column("Username")
                .name("Username")
                .filter("name").build()
                .build();
        assertThat(request.toString(), is("F_Username=name&ED_Username=true"));
    }

    @Test
    public void given_multiplecolumn_with_filter_expect_keyvalue() {
        RequestBuilder rBuilder = new RequestBuilder();
        RequestBuilder.Request request = rBuilder
                .column("Username")
                .name("Username")
                .filter("name").build()
                .column("Business")
                .name("Business")
                .filter("business").build()
                .column("Cust#")
                .name("Cust#")
                .filter("cust#").build()
                .column("Type")
                .name("Type")
                .filter("type").build()
                .column("Name")
                .name("Name")
                .filter("name").build()
                .column("Department")
                .name("Department")
                .filter("department").build()
                .build();
        assertThat(request.toString(), is("F_Username=name&ED_Username=true&F_Business=business&ED_Business=true&F_Cust#=cust#&ED_Cust#=true&F_Type=type&ED_Type=true&F_Name=name&ED_Name=true&F_Department=department&ED_Department=true"));
    }

}