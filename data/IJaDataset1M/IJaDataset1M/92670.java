package org.knopflerfish.bundle.console;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

/**
 * * Bundle activator implementation. * *
 * 
 * @author Anders Rimen *
 * @version $Revision: 1.1.1.1 $
 */
public class Activator implements BundleActivator {

    ServiceRegistration consoleReg;

    private static final String logServiceName = org.osgi.service.log.LogService.class.getName();

    private static final String consoleServiceName = org.knopflerfish.service.console.ConsoleService.class.getName();

    BundleContext bc;

    /**
     * * Called by the framework when this bundle is started. * *
     * 
     * @param bc
     *            Bundle context. *
     */
    public void start(BundleContext bc) {
        this.bc = bc;
        log(LogService.LOG_INFO, "Starting version " + bc.getBundle().getHeaders().get("Bundle-Version"));
        consoleReg = bc.registerService(consoleServiceName, new ConsoleServiceImpl(bc), null);
    }

    /**
     * * Called by the framework when this bundle is stopped. * *
     * 
     * @param bc
     *            Bundle context.
     */
    public void stop(BundleContext bc) {
        log(LogService.LOG_INFO, "Stopping");
    }

    /**
     * * Utility method used for logging. * *
     * 
     * @param level
     *            Log level *
     * @param msg
     *            Log message
     */
    void log(int level, String msg) {
        ServiceReference srLog = bc.getServiceReference(logServiceName);
        if (srLog != null) {
            LogService sLog = (LogService) bc.getService(srLog);
            if (sLog != null) {
                sLog.log(level, msg);
            }
            bc.ungetService(srLog);
        }
    }
}
