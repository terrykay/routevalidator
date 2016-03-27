package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class TableCell {
    private final int colSpan;

    public String getContents() {
        return contents;
    }

    public int getColSpan() {
        return colSpan;
    }

    private final String contents;

    public TableCell(int colSpan, String contents) {
        this.colSpan = colSpan;
        this.contents = contents;
    }
}
