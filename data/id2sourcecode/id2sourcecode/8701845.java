    public void setSamplePeriodChannel(final String pv) {
        final Channel oldSamplePeriodChannel = _samplePeriodChannel;
        if (oldSamplePeriodChannel != null && pv.equals(oldSamplePeriodChannel.channelName())) return;
        try {
            stopMonitoringSamplePeriod();
            if (oldSamplePeriodChannel != null) {
                oldSamplePeriodChannel.removeConnectionListener(this);
            }
            if (pv != null && pv.length() > 0 && !pv.equals("")) {
                _samplePeriodChannel = ChannelFactory.defaultFactory().getChannel(pv);
                _samplePeriodChannel.addConnectionListener(this);
                _samplePeriodChannel.requestConnection();
                Channel.flushIO();
            } else {
                _samplePeriodChannel = null;
            }
        } finally {
            BUSY_LOCK.unlock();
            SETTING_PROXY.settingChanged(this);
        }
    }
