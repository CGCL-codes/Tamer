    protected IDataLoaderFactory getFactory(String channelId) {
        Channel channel = configurationService.getChannel(channelId);
        String dataLoaderType = "default";
        IDataLoaderFactory factory = null;
        if (channel != null) {
            dataLoaderType = channel.getDataLoaderType();
        } else {
            log.warn("Could not locate the channel with the id of '{}'.  Using the 'default' data loader.", channelId);
        }
        factory = dataLoaderFactories.get(dataLoaderType);
        if (factory == null) {
            log.warn("Could not find a data loader factory of type '{}'.  Using the 'default' data loader.", dataLoaderType);
            factory = dataLoaderFactories.get("default");
        }
        if (!factory.isPlatformSupported(platform)) {
            log.warn("The current platform does not support a data loader type of '{}'.  Using the 'default' data loader.", dataLoaderType);
            factory = dataLoaderFactories.get("default");
        }
        return factory;
    }
