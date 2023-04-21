    public void connect() {
        source = new SiteSource(srcConfig, fbListener);
        connected = source.connect();
        chnlIndex = source.getChannelIndicies();
        chnlNames = source.getChannelNames();
    }
