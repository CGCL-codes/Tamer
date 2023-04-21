package net.sf.opendf.util.exception;

/**
 * The UnravelingExceptionHandler is a lightweight implementation of
 * the ExceptionHandler interface which uses a collection of handlers
 * to process each throwable in the causation stack of the processed
 * Throwable. 
 *
 * <p>Created: Tue Oct 09 13:17:43 2007
 *
 * @author imiller, last modified by $Author: imiller $
 * @version $Id:$
 */
public abstract class UnravelingExceptionHandler implements ExceptionHandler {

    public boolean process(Throwable t) {
        final ExceptionHandler handlers[] = getHandlers();
        while (t != null) {
            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i].process(t)) break;
            }
            t = t.getCause();
        }
        return true;
    }

    protected abstract ExceptionHandler[] getHandlers();
}
