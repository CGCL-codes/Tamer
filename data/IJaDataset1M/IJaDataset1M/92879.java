package frost.gui;

/**
 * Simple helper class that provides Strings for a rating.
 */
public class RatingStringProvider {

    public static final String[] ratingStrings = { "", "++", "+", "+/-", "-", "--" };

    public static String getRatingString(int rating) {
        if (rating < 0 || rating > ratingStrings.length) {
            return "*err*";
        }
        return ratingStrings[rating];
    }
}
