package com.bjt.routevalidator;

import java.util.List;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public interface Statistic {
    List<? extends TableCell> getCells();

    List<TableCell[]> getAcceptanceRows();
}

