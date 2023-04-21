package net.sf.laas.spectification.spi;

/**
 * This interface describes the requirements which a native log factory must
 * fulfill.
 */
public interface NativeLogFactory {

    /**
     * Deliver for the given categorie a {@link NativeLog}-object, which should
     * be for the same category the same object.
     * 
     * @param category category of the log
     * @return a {@link NativeLog} fpr the given category.
     */
    public NativeLog createLog(final String category);

    /**
     * Writes all logging events to the storage system, after this it is still
     * ok that the the native logsystem doesn't accept further logevents.
     */
    public void shutdown();
}
