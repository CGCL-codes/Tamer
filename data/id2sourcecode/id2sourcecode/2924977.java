    public void setChannelNameQuietly(String chanName) {
        if (chanName != null) {
            Channel chIn = ChannelFactory.defaultFactory().getChannel(chanName);
            setChannelQuietly(chIn);
        } else {
            stopMonitor();
            ch = null;
        }
    }
