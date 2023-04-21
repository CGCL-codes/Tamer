    public void setDelayChannel(final String pv) {
        final Channel oldDelayChannel = _delayChannel;
        if (oldDelayChannel != null && pv.equals(oldDelayChannel.channelName())) return;
        try {
            _isReady = false;
            BUSY_LOCK.lock();
            stopMonitoringDelay();
            if (oldDelayChannel != null) {
                oldDelayChannel.removeConnectionListener(this);
            }
            if (pv != null && pv.length() > 0 && !pv.equals("")) {
                _delayChannel = ChannelFactory.defaultFactory().getChannel(pv);
                _delayChannel.addConnectionListener(this);
                _delayChannel.requestConnection();
                Channel.flushIO();
            } else {
                _delayChannel = null;
            }
        } finally {
            BUSY_LOCK.unlock();
            SETTING_PROXY.settingChanged(this);
        }
    }
