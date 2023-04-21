package logahawk.formatters;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import org.testng.*;
import org.testng.annotations.*;

public class DateArgFormatterTest {

    @Test
    public void canFormat() {
        DateArgFormatter arg = new DateArgFormatter();
        Assert.assertTrue(arg.canFormat(new Date()));
        Assert.assertTrue(arg.canFormat(new java.sql.Date(100000)));
        Assert.assertTrue(arg.canFormat(new Time(100000)));
        Assert.assertTrue(arg.canFormat(new Timestamp(100000)));
        Assert.assertFalse(arg.canFormat(TimeZone.getDefault()));
        Assert.assertFalse(arg.canFormat(10000));
    }

    @Test
    public void customDateFormat() throws ParseException {
        DateArgFormatter arg = new DateArgFormatter(DateArgFormatter.STD_DATE_FORMAT);
        Date d = new Date();
        String v = arg.format(d, new ArrayList<ArgumentFormatter>(), 0);
        Assert.assertEquals(d, arg.getDateFormat().parse(v));
    }

    @Test
    public void defaultCtor() throws ParseException {
        DateArgFormatter arg = new DateArgFormatter();
        Date d = new Date();
        String v = arg.format(d, new ArrayList<ArgumentFormatter>(), 0);
        Assert.assertEquals(d, arg.getDateFormat().parse(v));
    }
}
