package com.gopai;

/**
 * Created by jemima.nyamogo on 5/24/2017.
 */
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
