    public void initialize(PluginInterface _plugin_interface) {
        plugin_interface = _plugin_interface;
        log = plugin_interface.getLogger().getChannel("ShareHosterPlugin");
        log.log(LoggerChannel.LT_INFORMATION, "ShareHosterPlugin: initialisation starts");
        plugin_interface.addListener(this);
    }
