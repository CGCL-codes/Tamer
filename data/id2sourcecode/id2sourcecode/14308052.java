    public void setChannels(final Element channels) throws ClassNotFoundException, AdapterManagementException {
        this.l_channels.clear();
        final DefaultConfigLoaderProvider defaultCfgLoader = ConfigurationDigester.getDefaultConfigLoaderProvider();
        this.l_channels.addAll(defaultCfgLoader.getChannels(channels));
    }
