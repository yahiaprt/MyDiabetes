package com.yahia.healthysiabires.future.datetemps;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class DateTimeUtilsTest {

    @Test
    public void calculatesQuarterCorrectly() {
        DateTime dateTime = DateTime.now();
        DateTime startOfQuarter = choisirletimeUS.withStartOfQuarter(dateTime.withMonthOfYear(2));
        Assert.assertEquals(1, startOfQuarter.monthOfYear().get());

        startOfQuarter = choisirletimeUS.withStartOfQuarter(dateTime.withMonthOfYear(5));
        Assert.assertEquals(4, startOfQuarter.monthOfYear().get());

        startOfQuarter = choisirletimeUS.withStartOfQuarter(dateTime.withMonthOfYear(8));
        Assert.assertEquals(7, startOfQuarter.monthOfYear().get());

        startOfQuarter = choisirletimeUS.withStartOfQuarter(dateTime.withMonthOfYear(11));
        Assert.assertEquals(10, startOfQuarter.monthOfYear().get());
    }
}
