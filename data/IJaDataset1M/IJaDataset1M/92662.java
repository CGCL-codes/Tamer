package net.sf.odinms.client.messages.commands;

import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.messages.Command;
import net.sf.odinms.client.messages.CommandDefinition;
import net.sf.odinms.client.messages.CommandProcessor;
import net.sf.odinms.client.messages.IllegalCommandSyntaxException;
import net.sf.odinms.client.messages.MessageCallback;
import net.sf.odinms.server.ShutdownServer;

public class ShutdownCommands implements Command {

    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        if (splitted[0].equals("!shutdown")) {
            int time = 60000;
            if (splitted.length > 1) {
                time = Integer.parseInt(splitted[1]) * 60000;
            }
            CommandProcessor.forcePersisting();
            c.getChannelServer().shutdown(time);
        } else if (splitted[0].equals("!shutdownworld")) {
            int time = 60000;
            if (splitted.length > 1) {
                time = Integer.parseInt(splitted[1]) * 60000;
            }
            CommandProcessor.forcePersisting();
            c.getChannelServer().shutdownWorld(time);
        } else if (splitted[0].equals("!shutdownnow")) {
            CommandProcessor.forcePersisting();
            new ShutdownServer(c.getChannel()).run();
        }
    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[] { new CommandDefinition("shutdown", "[when in Minutes]", "Shuts down the current channel - don't use atm", 10), new CommandDefinition("shutdownnow", "", "Shuts down the current channel now", 10), new CommandDefinition("shutdownworld", "[when in Minutes]", "Cleanly shuts down all channels and the loginserver of this world", 10) };
    }
}
