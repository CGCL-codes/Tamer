package org.tolven.plugin;

import org.java.plugin.Plugin;
import org.java.plugin.registry.PluginDescriptor;
import org.tolven.command.TolvenApplication;
import org.tolven.config.model.TolvenConfigWrapper;

public abstract class TolvenCommandPlugin extends TolvenPlugin {

    private static TolvenConfigWrapper tolvenConfigWrapper;

    public abstract void execute(String[] args) throws Exception;

    public static TolvenConfigWrapper getTolvenConfigWrapper() {
        if (tolvenConfigWrapper == null) {
            tolvenConfigWrapper = new TolvenConfigWrapper();
        }
        return tolvenConfigWrapper;
    }

    protected char[] getPassword(String refId) {
        return getTolvenConfigWrapper().getPasswordHolder().getPassword(refId);
    }

    public static void setTolvenConfigWrapper(TolvenConfigWrapper tcw) {
        tolvenConfigWrapper = tcw;
    }

    protected void execute(String pluginId, String[] args) {
        TolvenApplication.execute(pluginId, getManager(), args);
    }

    protected void execute(PluginDescriptor pluginDescriptor, String[] args) {
        TolvenApplication.execute(pluginDescriptor, getManager(), args);
    }

    protected Plugin getPlugin(PluginDescriptor pluginDescriptor) {
        return TolvenApplication.getPlugin(pluginDescriptor, getManager());
    }
}
