package net.sourceforge.esw.service.log;

import java.util.*;
import net.sourceforge.esw.graph.*;
import net.sourceforge.esw.service.*;
import net.sourceforge.esw.transport.Transport;
import net.jini.core.lookup.*;
import net.jini.core.entry.*;
import net.jini.lookup.entry.*;

/**
 * Describes the methods available for logging events from the enterprise.
 * <p>
 *
 * This is the Reference ILog implementation provided as a turn-key solution.
 * It provides compatablity with JDBC compliant drivers.
 * <p>
 *
 * There are two ways to log using an this ILog instance. The first is to use
 * the <code>log(INode aNode)</code> method. This requires that the INode
 * instance contains INode instances that have the correct ID and data types.
 * The second way is to use the <code>log(ILogDatum aDatum)</code> method.
 * This is the preferred way to log data. Create a ILogDatum instance using the
 * <code>LogFactory.createLogDatum()</code> method, then pass that ILogDatum
 * instance to this ILog instance.
 * <p>
 *
 * To create an instance of this ILog, see
 * <code>LogFactory.getLog()</code>.
 * <p>
 *
 * Examples:
 * <pre>
 *    ILog log = LogFactory.getLog();
 *    ILogDatum datum = LogFactory.createLogDatum( new Date(),             // when
 *                                                 "Machine Named George", // where
 *                                                 5,                      // importance
 *                                                 "A bad thing happened", // simple what
 *                                                 myNode,                 // complex what
 *                                                 "My App" );             // who
 *    log.log( datum );
 * </pre>
 * <p>
 *
 * @see net.sourceforge.esw.service.log.ILog
 * @see net.sourceforge.esw.service.log.LogFactory
 *
 */
public class SystemOutLog extends ALog implements ILog {

    /****************************************************************************
   * Creates a new SystemOutLog.
   */
    public SystemOutLog() throws Exception {
        super("SystemOutLog");
        Transport.startup();
        ILog log = (ILog) Transport.getProxy(this);
        register(new LogProxy(log), getEntries());
        start();
        System.out.println("SystemOutLog: SystemOutLog ready");
    }

    /****************************************************************************
   * Logs the specified INode instance to this ILog instance.
   *
   * @param aNode the INode instance containing the data of the event to be
   *        logged
   */
    public void log(INode aNode) {
        StringBuffer output = new StringBuffer("LogNode:");
        INode holder = aNode.get(ILogConstants.ID_ID);
        if (null != holder) {
            output.append("Id/");
            output.append(holder.getValue().toString());
            output.append(":");
        }
        holder = aNode.get(ILogConstants.WHEN_ID);
        if (null != holder) {
            output.append("When/");
            output.append(holder.getValue().toString());
            output.append(":");
        }
        holder = aNode.get(ILogConstants.WHERE_ID);
        if (null != holder) {
            output.append("Where/");
            output.append(holder.getValue().toString());
            output.append(":");
        }
        holder = aNode.get(ILogConstants.RANK_ID);
        if (null != holder) {
            output.append("Rank/");
            output.append(holder.getValue().toString());
            output.append(":");
        }
        holder = aNode.get(ILogConstants.DESC_ID);
        if (null != holder) {
            output.append("Desc/");
            output.append(holder.getValue().toString());
            output.append(":");
        }
        holder = aNode.get(ILogConstants.SERVICE_ID);
        if (null != holder) {
            output.append("ServiceID/");
            output.append(holder.getValue().toString());
            output.append(":");
        }
        holder = aNode.get(ILogConstants.APP_ID);
        if (null != holder) {
            output.append("AppID/");
            output.append(holder.getValue().toString());
            output.append(":");
        }
        holder = aNode.get(ILogConstants.DATA_ID);
        if (null != holder) {
            output.append("Data/");
            output.append(holder.getValue() + "");
            output.append(":");
        }
        System.out.println(output.toString());
    }

    /****************************************************************************
   * Logs the specified aDatum instance to this ILog instance.
   *
   * @param aDatum the aDatum instance containing the data of the event to be
   *        logged
   */
    public void log(ILogDatum aDatum) {
        log(LogUtil.convertDatumToNode(aDatum));
    }

    /****************************************************************************
   * Main entry point into this SystemOutLog
   */
    public static void main(String[] abcd) throws Exception {
        new SystemOutLog();
    }

    public static Entry[] getEntries() {
        ServiceInfo info = new ServiceInfo("SystemOutLog", "ESW Inc", "ESW Inc", "0.3", "A", "");
        net.jini.lookup.entry.Name name = new net.jini.lookup.entry.Name("SystemOutLog");
        Entry[] returnValue = { info, name };
        return returnValue;
    }
}
