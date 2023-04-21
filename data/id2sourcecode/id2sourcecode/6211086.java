    protected void addPlugin() throws TGPluginException {
        if (!this.loaded) {
            TuxGuitar.instance().getChannelManager().getChannelSettingsHandlerManager().addChannelSettingsHandler(this.handler);
            this.loaded = true;
        }
    }
