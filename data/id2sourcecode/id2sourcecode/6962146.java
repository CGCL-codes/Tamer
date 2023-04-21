    public void initialize(PluginInterface _plugin_interface) {
        plugin_interface = _plugin_interface;
        plugin_interface.getPluginProperties().setProperty("plugin.version", "1.0");
        plugin_interface.getPluginProperties().setProperty("plugin.name", "Plugin Updater");
        log = plugin_interface.getLogger().getChannel("Plugin Update");
        UIManager ui_manager = plugin_interface.getUIManager();
        final BasicPluginViewModel model = ui_manager.createBasicPluginViewModel(PLUGIN_RESOURCE_ID);
        final PluginConfig plugin_config = plugin_interface.getPluginconfig();
        boolean enabled = plugin_config.getPluginBooleanParameter("enable.update", true);
        model.setConfigSectionID(PLUGIN_CONFIGSECTION_ID);
        model.getStatus().setText(enabled ? "Running" : "Optional checks disabled");
        model.getActivity().setVisible(false);
        model.getProgress().setVisible(false);
        log.addListener(new LoggerChannelListener() {

            public void messageLogged(int type, String message) {
                model.getLogArea().appendText(message + "\n");
            }

            public void messageLogged(String str, Throwable error) {
                model.getLogArea().appendText(error.toString() + "\n");
            }
        });
        BasicPluginConfigModel config = ui_manager.createBasicPluginConfigModel(ConfigSection.SECTION_PLUGINS, PLUGIN_CONFIGSECTION_ID);
        config.addBooleanParameter2("enable.update", "Plugin.pluginupdate.enablecheck", true);
        plugin_interface.addEventListener(new PluginEventListener() {

            public void handleEvent(PluginEvent ev) {
                if (ev.getType() == PluginEvent.PEV_ALL_PLUGINS_INITIALISED) {
                    plugin_interface.removeEventListener(this);
                    initComplete(plugin_config);
                }
            }
        });
    }
