package com.choicemaker.shared.core;

import com.choicemaker.shared.api.CMPair;

/**
 * Represents a pair records. The order of records within a pair is not
 * important; see the {@link #equals(Object))} method.
 * 
 * @author chirag
 * @author rphall (added methods for equals and hashCode, adapted from
 *         http://forums.sun.com/thread.jspa?threadID=5132045)
 * @param <T>
 *            Typically, a bean representing a fielded data record.
 */
public class CMDefaultPair<T> extends CMStrictPair<T> {

    public CMDefaultPair(T left, T right) {
        super(left, right);
    }

    @Override
    public boolean equals(Object o) {
        boolean retVal = o == this ? true : false;
        if (!retVal && o instanceof CMDefaultPair<?>) {
            final CMPair<?> other = (CMPair<?>) o;
            retVal = (equal(getLeft(), other.getLeft()) && equal(getRight(), other.getRight()));
            retVal = retVal || (equal(getLeft(), other.getRight()) && equal(getRight(), other.getLeft()));
        }
        return retVal;
    }
}
