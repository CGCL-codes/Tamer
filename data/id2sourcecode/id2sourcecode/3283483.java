    public void setAccelerator(final Accelerator accelerator) {
        if (accelerator == _accelerator) {
            return;
        }
        _accelerator = accelerator;
        disposeRepRateWrapper();
        _repRate = Double.NaN;
        if (accelerator == null) {
            return;
        }
        try {
            Channel channel = accelerator.getTimingCenter().getChannel(TimingCenter.REP_RATE_HANDLE);
            _repRateWrapper = new ChannelWrapper(channel);
            _repRateWrapper.addChannelEventListener(this);
            _repRateWrapper.requestConnection();
        } catch (NoSuchChannelException exception) {
            System.out.println("No rep-rate channel available...");
        }
    }
