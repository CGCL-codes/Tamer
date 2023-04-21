    public void setChannelName(String chanName) {
        if (chanName != null) {
            Channel chIn = ChannelFactory.defaultFactory().getChannel(chanName);
            setChannel(chIn);
        } else {
            stopMonitor();
            ch = null;
            currValue = 0.;
        }
    }
