package com.gopai;

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

    public class Column {
        String name;
        String filter;
        boolean isVisible = true;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean visible) {
            isVisible = visible;
        }
    }
}
