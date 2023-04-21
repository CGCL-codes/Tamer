    public void setChannel(String channelName) throws ChannelSetException {
        isSettingChannel = true;
        try {
            if (channel != null) {
                channel.removeConnectionListener(this);
            }
            if (channelName == null) {
                setEnabled(false);
                channel = null;
                return;
            }
            channel = ChannelFactory.defaultFactory().getChannel(channelName);
            setEnabled(false);
            triggerChangeProxy.channelStateChanged(this);
            channel.addConnectionListener(this);
            setEnabled(true);
            settingProxy.settingChanged(this);
        } finally {
            isSettingChannel = false;
            triggerChangeProxy.channelStateChanged(this);
        }
    }
