package com.bjt.routevalidator;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ben.Taylor on 03/04/2016.
 */
public class Me {
    public static <T> T Last(List<? extends T> items) {
        final T last = items.get(items.size() - 1);
        return last;
    }
}
