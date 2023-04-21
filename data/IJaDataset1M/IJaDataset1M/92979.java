package org.gamegineer.game.core;

import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
final class Messages extends NLS {

    /** The name of the associated resource bundle. */
    private static final String BUNDLE_NAME = "org.gamegineer.game.core.Messages";

    /** A game implementation is not available for the requested configuration. */
    public static String GameFactory_createGame_unsupportedConfiguration;

    /**
     * Initializes the {@code Messages} class.
     */
    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
     * Initializes a new instance of the {@code Messages} class.
     */
    private Messages() {
        super();
    }
}
