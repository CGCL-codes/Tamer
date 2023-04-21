package org.jcvi.io.idReader;

/**
 * @{code StringIdParser} is an implementation of
 * {@link IdParser} for Strings.
 * @author dkatzel
 *
 *
 */
public class StringIdParser implements IdParser<String> {

    /**
     * Any non-null string is valid.
     * @return {@code true} if given string is not null;
     * {@code false} otherwise.
     */
    @Override
    public boolean isValidId(String string) {
        return string != null;
    }

    /**
     * Returns the given non-null value as the Id.
     * @return the given value.
     * @throws NullPointerException if given string is null.
     */
    @Override
    public String parseIdFrom(String string) {
        if (string == null) {
            throw new NullPointerException("string can not be null");
        }
        return string;
    }
}
