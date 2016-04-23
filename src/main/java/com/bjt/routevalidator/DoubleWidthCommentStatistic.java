package com.bjt.routevalidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleWidthCommentStatistic implements Statistic {

    private final String comment;

    public DoubleWidthCommentStatistic(final String comment) {
        this.comment = comment;
    }

    @Override
    public List<? extends TableCell> getCells() {
        return Arrays.asList(new TableCell(2, comment));
    }

    @Override
    public List<TableCell[]> getAcceptanceRows() {
        return new ArrayList<>();
    }
}