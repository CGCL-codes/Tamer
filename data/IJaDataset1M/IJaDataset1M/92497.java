package hu.akarnokd.reactiv4java;

/**
 * The observer who receives the notifications of T.
 * @author akarnokd
 * @param <T> the type of the notification values.
 */
public interface Observer<T> {

    /** 
	 * The next value is received. 
	 * @param value the next value 
	 */
    void next(T value);

    /** 
	 * An exception is received.
	 * @param ex the exception 
	 */
    void error(Throwable ex);

    /** No more values to expect. */
    void finish();
}
