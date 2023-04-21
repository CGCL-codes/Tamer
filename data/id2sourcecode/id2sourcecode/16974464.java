    public void initialize(PluginInterface _pi) {
        plugin_interface = _pi;
        singleton = this;
        init_sem.release();
        LoggerChannel log = plugin_interface.getLogger().getChannel("Plugin Test");
        log.log(LoggerChannel.LT_INFORMATION, "Plugin Initialised");
        plugin_interface.addListener(this);
    }
