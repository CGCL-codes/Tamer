package org.jxpfw.util;

import java.util.Random;

/**
 * Class to generate {@link Long} objects that are unique within the lifetime of
 * this constructed class. An attempt will be made to generate uniqueness
 * lasting more than a single lifetime, but no guarantees will be given.
 * @author rens.van.leeuwen@leanapps.com
 * @version 1.0 $Revision: 1.5 $
 */
public final class UniqueLongGenerator {

    /**
     * The singleton instance of this class.
     */
    private static final UniqueLongGenerator instance = new UniqueLongGenerator();

    /**
     * The last returned value.
     */
    private long lastReturnedValue;

    /**
     * Constructs a new UniqueLongGenerator object. Made private so that only a
     * single instance will be created. Retrieve the current instance by using
     * the {@link UniqueLongGenerator#getInstance()} method.
     */
    private UniqueLongGenerator() {
        final Random random = new Random();
        this.lastReturnedValue = random.nextLong();
    }

    /**
     * Returns the instance of the UniqueLongGenerator class.
     * @return The instance of the UniqueLongGenerator class.
     */
    public static UniqueLongGenerator getInstance() {
        return instance;
    }

    /**
     * Returns an unique Long object.
     * @return An unique Long object.
     */
    public synchronized Long getUniqueLong() {
        this.lastReturnedValue++;
        return Long.valueOf(this.lastReturnedValue);
    }
}
