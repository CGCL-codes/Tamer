package net.sf.msnalert.command.admin;

import java.util.Set;
import net.sf.msnalert.command.user.*;
import net.sf.msnalert.command.Command;
import net.sf.jml.Email;
import net.sf.msnalert.command.CommandsHelp;
import net.sf.msnalert.command.CommandsHelpLocator;
import net.sf.msnalert.i18n.ResourceBundleLocator;

/**
 *
 * @author Mauro Codella
 */
public class CommandsListCommand extends Command {

    @Override
    public String handle(String[] args, Email email) {
        String help = "\n";
        CommandsHelp commandsHelp = CommandsHelpLocator.getAdminHelp();
        for (Command command : commandsHelp.getCommands()) {
            Set<String> aliases = command.getCommandNames();
            help += getFormattedAliases(aliases) + " - " + commandsHelp.getHelp(command) + ";\n";
        }
        return help;
    }

    private String getFormattedAliases(Set<String> aliases) {
        String formatted = "|";
        for (String alias : aliases) formatted += alias + "|";
        return formatted;
    }

    @Override
    public Set<String> getCommandNames() {
        return getAliases("commandlist", "cl", "?");
    }

    @Override
    public String getHelp() {
        return ResourceBundleLocator.getResourceBundle().getString("prints_this_command_list");
    }
}
