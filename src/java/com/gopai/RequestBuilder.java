package com.gopai;

/**
 * Created by jemima.nyamogo on 5/25/2017.
 */
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
}
