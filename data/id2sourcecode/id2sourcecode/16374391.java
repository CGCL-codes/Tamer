    public TGChannelSettingsDialog findChannelSettingsDialog() {
        TGChannelSettingsHandler channelSettingsHandler = findSupportedChannelSettingsHandler();
        if (channelSettingsHandler != null) {
            return channelSettingsHandler.getChannelSettingsDialog();
        }
        return null;
    }
