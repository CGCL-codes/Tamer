package ie.ucd.lis.ojax;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermEnum;
import org.apache.log4j.Logger;

/**
 * Finds the years represented in the repository.
 *
 * @author P�draig O'hIceadha
 */
public class DateTerms {

    private static Logger logger = Logger.getLogger(DateTerms.class);

    private SortedMap years;

    private int earliestYear = -1;

    private int latestYear = -1;

    /** Creates a new instance of DateTerms.
     *  @param indexPath The location of the Lucence index to search for years
     */
    public DateTerms(String indexPath) {
        years = new TreeMap();
        this.findDateTerms(indexPath);
    }

    /**
     * Scan all terms in 'date' field and extract all years.
     */
    public void findDateTerms(String indexPath) {
        StringBuffer result = new StringBuffer(512);
        IndexReader reader = null;
        Calendar calendar = new GregorianCalendar();
        try {
            reader = IndexReader.open(indexPath);
            TermEnum terms = reader.terms();
            while (terms.next()) {
                String fieldName = terms.term().field();
                if ("date".equals(fieldName)) {
                    Integer year = extractYear(terms.term().text(), calendar);
                    if (year != null) {
                        years.put(year, year);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed processing Lucene date terms", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                logger.error("Failed to close IndexReader", ex);
            }
        }
        if (years.size() > 0) {
            Integer startYear = (Integer) years.firstKey();
            if (startYear != null) {
                earliestYear = startYear.intValue();
            }
            Integer endYear = (Integer) years.lastKey();
            if (endYear != null) {
                setLatestYear(endYear.intValue());
            }
        }
    }

    public Integer extractYear(String dateStr, Calendar calendar) {
        Integer year = null;
        try {
            Date date = DateTools.stringToDate(dateStr);
            calendar.setTime(date);
            year = new Integer(calendar.get(Calendar.YEAR));
        } catch (java.text.ParseException pe) {
            logger.error("Failed to parse date " + dateStr);
        }
        return year;
    }

    public int getEarliestYear() {
        return earliestYear;
    }

    public Iterator iterator() {
        return years.keySet().iterator();
    }

    public String toString() {
        StringBuffer result = new StringBuffer(64);
        Iterator itr = years.keySet().iterator();
        while (itr.hasNext()) {
            Integer year = (Integer) itr.next();
            result.append(year);
            result.append("\n");
        }
        return result.toString();
    }

    public int getLatestYear() {
        return latestYear;
    }

    public void setLatestYear(int latestYear) {
        this.latestYear = latestYear;
    }
}
