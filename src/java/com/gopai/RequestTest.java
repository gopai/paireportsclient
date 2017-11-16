package com.gopai;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by jemima.nyamogo on 5/31/2017.
 */
public class RequestTest {

    @Test
    public void given_singlecolumn_with_filter_expect_keyvalue() {
        RequestBuilder rBuilder = new RequestBuilder();
        Request request = rBuilder
                .column("Username")
                .name("Username")
                .filter("name").build()
                .build();
        assertThat(request.toString(), is("F_Username=name"));
    }

    @Test
    public void test() {
        RequestBuilder rBuilder = new RequestBuilder();
        Request request = rBuilder
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
        assertThat(request.toString(), is("F_Username=name&F_Business=business&F_Cust#=cust#&F_Type=type&F_Name=name&F_Department=department"));
    }

}