    protected void removePlugin() throws TGPluginException {
        if (this.loaded) {
            TuxGuitar.instance().getChannelManager().getChannelSettingsHandlerManager().removeChannelSettingsHandler(this.handler);
            this.loaded = false;
        }
    }
