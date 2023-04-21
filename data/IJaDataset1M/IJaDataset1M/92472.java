package com.antlersoft.query.environment;

/**
 * @author Michael A. MacDonald
 *
 */
public class QueryException extends Exception {

    /**
	 * 
	 */
    public QueryException() {
        super();
    }

    /**
	 * @param message
	 */
    public QueryException(String message) {
        super(message);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param cause
	 */
    public QueryException(Throwable cause) {
        super(cause);
    }
}
