package com.kapil.framework.concurrent;

/**
 * Interface for the broker which acts as the contact/data exchanger between the {@link IProducer} and {@link IConsumer}
 * 
 * @author Kapil Viren Ahuja
 * 
 * @param <V>
 */
public interface IBroker<V> {

    /**
     * Puts the data in the underlying data storage
     * 
     * @param data
     * @throws InterruptedException
     */
    void put(V data) throws InterruptedException;

    /**
     * Fetches the data from the underlying storage
     * 
     * @return
     * @throws InterruptedException
     */
    V get() throws InterruptedException;

    /**
     * <p>
     * Returns the status is the {@link IProducer} is still producing the data. This is used by the {@link IConsumer} to
     * decide how long they should keep running.
     * </p>
     * 
     * <p>
     * If it returns <Code>true</Code> then the {@link IProducer} is still producing, if <Code>false</Code> then the
     * producer is not producing.
     * </p>
     * 
     * @return
     */
    Boolean isProducing();

    /**
     * Updates the status for the {@link IProducer} state. Calling this function will set the
     * {@link IBroker#isProducing()} to <code>false</code>
     */
    void stopProducing();

    /**
     * <p>
     * Updates the status for the {@link IProducer} state. Calling this function will set the
     * {@link IBroker#isProducing()} to <code>true</code>.
     * </p>
     * 
     * <p>
     * This is helpful especially if the consumers have records which failed the first time
     * and they need processing again.
     * </p>
     */
    void startProducing();
}
