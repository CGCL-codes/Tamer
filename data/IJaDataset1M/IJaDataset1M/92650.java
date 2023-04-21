package org.jquantlib.time.calendars;

import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * @author Srinivas Hasti
 * @author Dominik Holenstein
 * 
 */
public class Switzerland extends DelegateCalendar {

    public static enum Market {

        SETTLEMENT, SWX
    }

    ;

    private static final Switzerland SETTLEMENT_CALENDAR = new Switzerland(Market.SETTLEMENT);

    private static final Switzerland SWX_CALENDAR = new Switzerland(Market.SWX);

    private Switzerland(Market market) {
        Calendar delegate;
        switch(market) {
            case SETTLEMENT:
                delegate = new SwisSettlementCalendar();
                break;
            case SWX:
                delegate = new SWXStockExchangeCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static Switzerland getCalendar(Market market) {
        switch(market) {
            case SETTLEMENT:
                return SETTLEMENT_CALENDAR;
            case SWX:
                return SWX_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }
}

final class SwisSettlementCalendar extends WesternCalendar {

    public String getName() {
        return "Swiss settlement";
    }

    public boolean isBusinessDay(Date date) {
        Weekday w = date.getWeekday();
        int d = date.getDayOfMonth(), dd = date.getDayOfYear();
        Month m = date.getMonthEnum();
        int y = date.getYear();
        int em = easterMonday(y);
        if (isWeekend(w) || (d == 1 && m == JANUARY) || (d == 2 && m == JANUARY) || (dd == em - 3) || (dd == em) || (dd == em + 38) || (dd == em + 49) || (d == 1 && m == MAY) || (d == 1 && m == AUGUST) || (d == 24 && m == DECEMBER) || (d == 25 && m == DECEMBER) || (d == 26 && m == DECEMBER) || (d == 31 && m == DECEMBER)) return false;
        return true;
    }
}

final class SWXStockExchangeCalendar extends WesternCalendar {

    public String getName() {
        return "SWX stock exchange";
    }

    public boolean isBusinessDay(Date date) {
        Weekday w = date.getWeekday();
        int d = date.getDayOfMonth(), dd = date.getDayOfYear();
        Month m = date.getMonthEnum();
        int y = date.getYear();
        int em = easterMonday(y);
        if (isWeekend(w) || (d == 1 && m == JANUARY) || (d == 2 && m == JANUARY) || (dd == em - 3) || (dd == em) || (d == 1 && m == MAY) || (dd == em + 38) || (dd == em + 49) || (d == 1 && m == AUGUST) || (d == 25 && m == DECEMBER) || (d == 26 && m == DECEMBER)) return false;
        return true;
    }
}
