    public LogAdapter(PluginInterface pluginInterface) {
        this.loggerChannel = pluginInterface.getLogger().getChannel(LOG_CHANNEL);
    }
