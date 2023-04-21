    public void initialize(PluginInterface aPlugin) {
        ConfigEntry entry = new ConfigEntry(aPlugin.getPluginDirectoryName(), SafepeerConfigConstants.CONFIG_FILE_NAME);
        boolean isGuiEnabled = Boolean.valueOf(entry.getProperty("enable.gui")).booleanValue();
        if (isGuiEnabled) {
            aPlugin.getUIManager().getSWTManager().addView(new AzureusSafePeerView(entry));
        }
        boolean isEnabled = Boolean.valueOf(entry.getProperty(SafepeerConfigConstants.ENABLE_SAFEPEER)).booleanValue();
        if (isEnabled) {
            export(entry, aPlugin);
        } else {
            LoggerChannel logger = aPlugin.getLogger().getChannel("SafePeer");
            logger.log(LoggerChannel.LT_INFORMATION, "SafePeer is currently disabled. To enable SafePeer, change the safepeer.properties file to: enable.safepeer=true");
        }
    }
