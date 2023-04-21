package org.ensembl.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convenience functions for interacting with the logging system.
 * 
 * This class contains convenience methods for interacting with the
 * java.util.logging package.
 * 
 * Example:
 * <ol>If you want to print all FINE log messages for a class:
 * <li><code>LoggingUtil.setRootLoggerHandlersLevelToFINE()</code></li>
 * <li><code>Logger.getLogger("somepackage.MyClass").setLevel(Level.FINE)</code></li>
 * </ol>
 * 
 * @author <a href="mailto:craig@ebi.ac.uk">Craig Melsopp </a>
 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html">Logging system documentation</a>
 */
public class LoggingUtil {

    private LoggingUtil() {
    }

    /**
	 * Set the logging level for all the handlers on the root logger instance
	 * 
	 * For a log message to be printed it's level must be >= the logger's level
	 * AND >= the handlers level. This method sets the level for all the
	 * handlers on the root logger which, by default, apply to all loggers.
	 * 
	 * @param level
	 *            logging level for default handlers.
	 */
    public static void setRootLoggerHandlersLevels(Level level) {
        Logger l = Logger.getLogger("");
        Handler[] handlers = l.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].setLevel(level);
        }
    }

    /**
	 * Set the logging level for all the handlers of the specified type on the root logger instance
	 * 
	 * For a log message to be printed it's level must be >= the logger's level
	 * AND >= the handlers level. This method sets the level for all the
	 * handlers on the root logger which, by default, apply to all loggers.
	 * 
	 * @param level
	 *            logging level for default handlers.
	 * @param type type of handler, e.g. java.util.logging.ConsoleHandler
	 */
    public static void setRootLoggerHandlersLevels(Level level, Class type) {
        Logger l = Logger.getLogger("");
        Handler[] handlers = l.getHandlers();
        for (int i = 0; i < handlers.length; i++) if (handlers[i].getClass() == type) handlers[i].setLevel(level);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.ALL).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToALL() {
        setRootLoggerHandlersLevels(Level.ALL);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.CONFIG).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToCONFIG() {
        setRootLoggerHandlersLevels(Level.CONFIG);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.FINE).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToFINE() {
        setRootLoggerHandlersLevels(Level.FINE);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.FINER).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToFINER() {
        setRootLoggerHandlersLevels(Level.FINER);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.FINEST).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToFINEST() {
        setRootLoggerHandlersLevels(Level.FINEST);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.INFO).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToINFO() {
        setRootLoggerHandlersLevels(Level.INFO);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.SEVERE).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToSEVERE() {
        setRootLoggerHandlersLevels(Level.SEVERE);
    }

    /**
	 * Calls setRootLoggerHandlersLevels(Level.WARNING).
	 * 
	 * @see #setRootLoggerHandlersLevels(Level)
	 */
    public static void setRootLoggerHandlersLevelsToWARNING() {
        setRootLoggerHandlersLevels(Level.WARNING);
    }
}
