package com.gopai;

/**
 * Created by jemima.nyamogo on 5/24/2017.
 */
public class ColumnBuilder {
    RequestBuilder rBuilder;

    private String name;
    private String filter;
    private boolean visibility = true;

    public ColumnBuilder(RequestBuilder builder) {
        this.rBuilder = builder;

    }

    public ColumnBuilder name(String name) {
        this.name = name;
        return this;

    }

    public ColumnBuilder filter(String filter) {
        this.filter = filter;
        return this;

    }

    public ColumnBuilder visible(boolean visibility) {
        this.visibility = visibility;
        return this;

    }

    public RequestBuilder build() {
        Column column = new Column();
        column.name = name;
        column.filter = filter;
        column.isVisible = visibility;
        rBuilder.getRequest().columns.add(column);
        return rBuilder;
    }
}
