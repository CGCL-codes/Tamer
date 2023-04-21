package net.sf.jimo.api.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.sf.jimo.api.BundleService;
import net.sf.jimo.api.JIMOConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A helper class to aid developers when creating bundle activators.
 * Features of this class include logger creation, resource strings,
 * and property resolver.  Inheriting classes should declare a 
 * public static instance of the activator, to be used by other classes.
 * This saves always having to pass in the bundleContext, logging, etc.
 *  
 * @author logicfish@hotmail.com
 *
 */
public abstract class BundleActivatorHelper implements BundleActivator {

    protected ServiceTracker logTracker;

    protected Config config;

    protected BundleContext bundleContext;

    ResourceBundle resBundle;

    public void start(BundleContext context) throws Exception {
        this.bundleContext = context;
        logTracker = new ServiceTracker(context, LogService.class.getName(), null);
        logTracker.open();
        ServiceReference reference = context.getServiceReference(BundleService.class.getName());
        BundleService bundleService = (BundleService) context.getService(reference);
        resBundle = ResourceBundle.getBundle("Bundle", Locale.getDefault(), Thread.currentThread().getContextClassLoader());
        config = bundleService.createConfig(resBundle);
        getLogService().log(LogService.LOG_INFO, getResourceString(JIMOConstants.RES_MODULESTARTUP));
    }

    public LogService getLogService() {
        return (LogService) logTracker.getService();
    }

    public String getResourceString(String key) {
        try {
            return config.getResourceString(key);
        } catch (MissingResourceException e) {
            e.printStackTrace(System.err);
            return "?" + key + "?";
        }
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void stop(BundleContext context) throws Exception {
        getLogService().log(LogService.LOG_INFO, getResourceString(JIMOConstants.RES_MODULESTOP));
    }

    public Config getConfig() {
        return config;
    }
}
