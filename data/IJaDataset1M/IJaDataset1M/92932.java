package example.smartcache;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class of the directory client. Start some of this one after a server.
 * 
 * @author Franz J. Hauck
 *
 */
public class Client {

    /**
	 * The registry name for the directory object
	 */
    static final String regName = "rmi://localhost/Directory";

    /**
	 * The logger references
	 */
    static final Logger rlog = Logger.getLogger("");

    static final Logger log = Logger.getLogger("example");

    static final Logger flog = Logger.getLogger("org.aspectix.formi");

    public static void main(String[] args) {
        Directory d = null;
        System.out.println("Directory server started\n");
        for (Handler tmphdl : rlog.getHandlers()) {
            tmphdl.setLevel(Level.ALL);
        }
        rlog.setLevel(Level.OFF);
        flog.setLevel(Level.ALL);
        log.setLevel(Level.ALL);
        try {
            d = (Directory) Naming.lookup(regName);
        } catch (Throwable e1) {
            e1.printStackTrace();
            log.severe("Exception caught when looking up in registry: " + e1);
        }
        try {
            d.put("alpha", "A text for key alpha");
            d.put("beta", "A text for key beta");
            for (int i = 0; i < 10; i++) {
                String s = d.get("alpha");
                log.fine("d.get= " + s);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
