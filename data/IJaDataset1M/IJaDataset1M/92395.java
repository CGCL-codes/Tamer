package info.freundorfer.icalendar.datatypes;

import info.freundorfer.icalendar.ICalendarParseException;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Text according to RFC 5545: 3.1
 * 
 * @author Josef Freundorfer <http://www.freundorfer.info>
 */
public class ParamText extends DataType<ParamText> {

    private static char[] quoteChars = { ',', ';', '"' };

    private static Map<java.lang.Integer, java.lang.Integer> validCharacterRanges;

    /**
	 * initStaticTables()
	 * Initializes the static tables for validating characters and escaping strings
	 */
    private static void initStaticTables() {
        if (validCharacterRanges == null) {
            validCharacterRanges = new HashMap<java.lang.Integer, java.lang.Integer>();
            validCharacterRanges.put((int) '\t', (int) '\t');
            validCharacterRanges.put(32, 33);
            validCharacterRanges.put(35, 126);
            validCharacterRanges.put(128, 248);
            validCharacterRanges.put(256, java.lang.Integer.MAX_VALUE);
        }
    }

    private String value;

    public ParamText() {
        initStaticTables();
    }

    /**
	 * @param string
	 * @throws ICalendarParseException
	 */
    public ParamText(final String string) throws ICalendarParseException {
        initStaticTables();
        parse(string);
    }

    @Override
    public ParamText parse(final String value) throws ICalendarParseException {
        validateText(value);
        this.value = value;
        return this;
    }

    @Override
    public void serialize(final Writer w) throws IOException {
        w.append(value);
    }

    public ParamText setText(final String value) throws ICalendarParseException {
        validateText(value);
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean validateText(final String text) throws ICalendarParseException {
        final char[] chars = text.toCharArray();
        int start = 0;
        int end = chars.length;
        boolean isQuoted = false;
        if ((chars[0] == '"') && (chars[chars.length - 1] == '"')) {
            start++;
            end--;
            isQuoted = true;
        }
        for (int i = start; i < end; i++) {
            final char c = chars[i];
            boolean valid = false;
            for (final Entry<java.lang.Integer, java.lang.Integer> entry : validCharacterRanges.entrySet()) {
                if ((c >= entry.getKey()) && (c <= entry.getValue())) {
                    valid = true;
                    break;
                }
            }
            if (!isQuoted) {
                for (final char toBeQuoted : quoteChars) {
                    if (c == toBeQuoted) {
                        valid = false;
                    }
                }
            }
            if (!valid) {
                throw new ICalendarParseException("String contains illegal character \"" + c + "\" (" + (int) c + ")");
            }
        }
        return isQuoted;
    }
}
