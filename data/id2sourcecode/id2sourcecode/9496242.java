    public void update(final DataAdaptor adaptor) {
        if (adaptor.hasAttribute("bufferSize")) {
            setBufferSize(adaptor.intValue("bufferSize"));
        }
        if (adaptor.hasAttribute("binCount")) {
            setBinCount(adaptor.intValue("binCount"));
        }
        if (adaptor.hasAttribute("autoLimits")) {
            setAutoLimits(adaptor.booleanValue("autoLimits"));
        }
        if (adaptor.hasAttribute("manualLowerLimit") && adaptor.hasAttribute("manualUpperLimit")) {
            final double lowerLimit = adaptor.doubleValue("manualLowerLimit");
            final double upperLimit = adaptor.doubleValue("manualUpperLimit");
            final double[] range = new double[] { lowerLimit, upperLimit };
            setManualValueRange(range);
        }
        final ChannelSource channelSource = ChannelSource.getChannelSource(adaptor, _accelerator);
        setChannelSource(channelSource);
    }
