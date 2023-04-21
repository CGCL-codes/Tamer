package webservicesapi.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a history of commands.
 *
 * @author Ben Leov
 */
public class CommandTracker implements CommandParserListener {

    private List<String> commands;

    /**
     * The maximum number of commands to track
     */
    private static final int MAX_TRACK_SIZE = 100;

    public CommandTracker() {
        commands = new ArrayList<String>();
    }

    public String getCommand(int index) {
        if (commands.size() > index && index > -1) {
            return commands.get(index);
        } else {
            return null;
        }
    }

    @Override
    public void onLineRead(String read) {
        if (read != null && !read.trim().equals("")) {
            commands.add(read);
            if (commands.size() > MAX_TRACK_SIZE) {
                commands.remove(0);
            }
        }
    }
}
