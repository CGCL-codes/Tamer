    public void setChannel(final String channelName) throws ChannelSetException {
        try {
            _isReady = false;
            _isSettingChannel = true;
            BUSY_LOCK.lock();
            setEnabled(false);
            stopChannelEvents();
            _waveformDelayInitialized = false;
            _samplePeriod = 0;
            _waveformDelay = 0;
            if (channelName == null || channelName == "" || channelName.length() == 0) {
                _channel = null;
                return;
            }
            _channel = ChannelFactory.defaultFactory().getChannel(channelName);
            CHANNEL_MODEL_PROXY.disableChannel(this, _channel);
            _channel.addConnectionListener(this);
            _channel.requestConnection();
            setupTimeChannels();
            Channel.flushIO();
            setEnabled(true);
        } finally {
            BUSY_LOCK.unlock();
            _isSettingChannel = false;
            CHANNEL_MODEL_PROXY.channelChanged(this, _channel);
            SETTING_PROXY.settingChanged(this);
        }
    }
