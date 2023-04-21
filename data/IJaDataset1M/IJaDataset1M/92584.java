package org.epoline.service.legacy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.epoline.jsf.client.ServiceNotAvailableException;
import org.epoline.jsf.client.ServiceProvider;
import org.epoline.jsf.client.ServiceProviderAttribute;
import org.epoline.jsf.services.core.JiniService;
import org.epoline.monitoring.MonitoringFrame;
import org.epoline.service.legacy.dl.LegacyProxy;
import org.epoline.service.legacy.dl.LegacyProxyInterface;
import org.epoline.service.support.BaseServiceMonitor;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Class that will be used by the GUI Monitor
 */
public class LegacyServiceMonitor extends BaseServiceMonitor {

    public static final String DB_STATUS = "DBStatus";

    private String dbStatus;

    private ServiceProvider sp;

    private Logger log;

    /**
	 * ServiceMonitor constructor comment.
	 */
    public LegacyServiceMonitor(LegacyProxy proxy, Properties props, JiniService aJiniService, Logger log) throws IOException {
        super(proxy, proxy.getClass().getName(), Integer.parseInt(props.getProperty("org.epoline.monitoring.port", "0")), aJiniService);
        this.log = log;
        init();
        showProperties(props, "Properties");
        ServiceProvider.setContext(props);
        sp = ServiceProvider.getInstance();
        try {
            start();
        } catch (Exception e) {
            log.error("Error starting monitor", e);
        }
    }

    public String getDBStatus() {
        getData();
        return dbStatus;
    }

    public String getServerStatus() {
        try {
            ServiceProviderAttribute spa[] = { new ServiceProviderAttribute("hostname", InetAddress.getLocalHost().getHostName()) };
            sp.getService(LegacyProxyInterface.NAME, spa);
            if (log.isDebugEnabled()) log.debug("Service discovered");
            return MonitoringFrame.SERVICE_OK;
        } catch (UnknownHostException e) {
            log.fatal("Service not found", e);
            return MonitoringFrame.SERVICE_KO;
        } catch (ServiceNotAvailableException e) {
            log.fatal("Service not found", e);
            return MonitoringFrame.SERVICE_KO;
        } catch (ClassNotFoundException e) {
            log.fatal("Service not found", e);
            return MonitoringFrame.SERVICE_KO;
        }
    }

    protected void handleDataFromProxy(Document doc) {
        dbStatus = "???";
        try {
            Element e = doc.getRootElement();
            Attribute a1 = e.getAttribute(DB_STATUS);
            if (a1 != null) dbStatus = a1.getValue();
        } catch (Exception e) {
            log.fatal("Error handling data from proxy", e);
        }
    }

    protected void init() {
        try {
            addMethod("Runtime Info", "getDBStatus", "DB status");
        } catch (Exception e) {
            log.error("Error init monitor", e);
        }
    }
}
