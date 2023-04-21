package net.sf.eqemutils.utils;

import java.util.*;
import java.util.regex.*;

/** Collection of utility functions for string processing.
 */
public class StringUtils {

    public static String BulkReplaceAll(String ThisString, Vector<Pattern> OriginalPatterns, Vector<String> Replacements) {
        assert OriginalPatterns.size() == Replacements.size();
        String Result;
        Matcher ThisMatcher;
        int j, jEnd;
        Result = ThisString;
        for (j = 1, jEnd = OriginalPatterns.size(); j <= jEnd; j++) {
            ThisMatcher = OriginalPatterns.elementAt(j - 1).matcher(Result);
            Result = ThisMatcher.replaceAll(Matcher.quoteReplacement(Replacements.elementAt(j - 1)));
        }
        return Result;
    }

    public static Vector<String> BulkReplaceAll(Vector<String> TheseStrings, Vector<Pattern> OriginalPatterns, Vector<String> Replacements) {
        assert OriginalPatterns.size() == Replacements.size();
        Vector<String> Result;
        Matcher ThisMatcher;
        int j, jEnd;
        Result = new Vector<String>(TheseStrings.size());
        for (j = 1, jEnd = TheseStrings.size(); j <= jEnd; j++) Result.addElement(BulkReplaceAll(TheseStrings.elementAt(j - 1), OriginalPatterns, Replacements));
        return Result;
    }

    protected StringUtils() {
    }
}
