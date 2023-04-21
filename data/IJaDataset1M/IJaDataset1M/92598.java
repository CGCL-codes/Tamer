package edu.psu.citeseerx.exec.respmod.modifiers;

import edu.psu.citeseerx.utility.ConfigurationManager;
import edu.psu.citeseerx.exec.com.ObjectServer;
import edu.psu.citeseerx.exec.respmod.servicecommands.CoBlitzLinkerServiceCommand;

/**
 * @author Levent Bolelli
 *
 */
public class CoBlitzLinker {

    public CoBlitzLinker() throws Exception {
        ConfigurationManager cm = new ConfigurationManager();
        try {
            ObjectServer server = ObjectServer.createFromConfiguration(new CoBlitzLinkerServiceCommand(), cm);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
    }
}
