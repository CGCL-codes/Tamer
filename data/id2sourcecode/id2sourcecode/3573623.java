    private void configureSrcConfig(SensorMetaData smd, SrcConfig srcConfig) {
        if (srcConfig instanceof TurbineSrcConfig) {
            ((TurbineSrcConfig) srcConfig).resetChannelVecs(smd.getChannels(), smd.getChannelDatatypes());
        } else {
            throw new IllegalArgumentException("Currently only data turbine is " + "supported");
        }
    }
