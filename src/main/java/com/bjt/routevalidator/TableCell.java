package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class TableCell {
    private final int colSpan;

    public String getClasses() {
        return classes;
    }

    public String getContents() {
        return contents;
    }

    public int getColSpan() {
        return colSpan;
    }

    private final String contents;
    private final String classes;

    public TableCell(int colSpan, String contents) {
        this(colSpan, contents, null);
    }
    public TableCell(int colSpan, String contents, String classes) {
        this.colSpan = colSpan;
        this.contents = contents;
        this.classes = classes;
    }


}
