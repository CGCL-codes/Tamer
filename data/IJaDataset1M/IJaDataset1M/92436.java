package cz.cvut.fel.mvod.prologueServer;

import cz.cvut.fel.mvod.global.GlobalSettingsAndNotifier;

/**
 * A handler that has regirtration disabled
 * @author Radovan Murin
 */
public class ProvidingHandler extends registeringHandler {

    @Override
    public String parsePost(String body) {
        return GlobalSettingsAndNotifier.singleton.messages.getString("regsAreClosedTXT");
    }
}
