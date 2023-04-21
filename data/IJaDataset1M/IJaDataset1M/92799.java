package org.xaware.shared.util.logging;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import org.xaware.shared.util.BizSessionUtil;
import org.xaware.shared.util.ThreadBizSession;

/**
 * Format the log information in the following format 2007-02-08 14:06:13.764 0010 INFO
 */
public class SimpleLogFormatter extends Formatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final char DOT = '.';

    private static final String DOUBLE_COLON = "::";

    private static final char SPACE = ' ';

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final DecimalFormat dfmt = new DecimalFormat(" 0000");

    private static final char blankString[] = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };

    private String threadIDSuffix = null;

    public SimpleLogFormatter() {
        threadIDSuffix = LogConfigUtil.getInstance().getThreadIdSuffix();
    }

    /**
     * Format the log information in the following format 2007-02-08 14:06:13.764 0010 INFO .......
     */
    @Override
    public String format(final LogRecord inRec) {
        final StringBuffer buf = new StringBuffer();
        final Date tsDate = new Date(inRec.getMillis());
        format.format(tsDate, buf, new FieldPosition(0));
        buf.append(SPACE);
        String sessionId = ThreadBizSession.getSessionId();
        if (sessionId == null) {
            sessionId = BizSessionUtil.ZEROS;
        }
        buf.append(sessionId).append(DOT);
        buf.append(dfmt.format(inRec.getThreadID()).trim()).append(threadIDSuffix);
        buf.append(SPACE);
        final String levelString = inRec.getLevel().toString();
        buf.append(levelString);
        final int pad = 8 - levelString.length();
        buf.append(blankString, 0, pad);
        if ((inRec.getSourceClassName() != null) && (inRec.getSourceMethodName() != null)) {
            buf.append(inRec.getSourceClassName()).append(DOUBLE_COLON).append(inRec.getSourceMethodName()).append(SPACE);
        }
        buf.append(inRec.getMessage()).append(LINE_SEPARATOR);
        return buf.toString();
    }
}
