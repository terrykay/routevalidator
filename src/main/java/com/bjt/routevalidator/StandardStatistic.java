package com.bjt.routevalidator;

import java.util.Arrays;
import java.util.List;

public abstract class StandardStatistic implements Statistic {
    private final String name;
    private final String value;

    @Override
    public List<? extends TableCell> getCells() {
        return Arrays.asList(
                new TableCell(1, name),
                new TableCell(1, value)
        );
    }

    public StandardStatistic(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public List<TableCell[]> getAcceptanceRows() {
        return new TableCell[][]{};
    }
}

