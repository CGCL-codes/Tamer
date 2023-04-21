    public void initSrc(SrcConfig srcConfigVal) {
        if (!(srcConfigVal instanceof TurbineSrcConfig)) {
            throw new IllegalArgumentException("The config parameter has to be " + "an instance of TurbineSrcConfig");
        }
        this.srcConfig = (TurbineSrcConfig) srcConfigVal;
        this.srcChannelNames = srcConfig.getChannelNames();
        this.srcChannelDataTypes = srcConfig.getChannelDataTypes();
        src = new Source(srcConfig.cacheSize, srcConfig.archiveMode, srcConfig.archiveSize);
        setClient(src);
        this.srcChannelIndicies = addChannels(srcChannelNames);
    }
