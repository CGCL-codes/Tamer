    public void setChannelName(String chanName) {
        triggerNamePV = chanName;
        ch = ChannelFactory.defaultFactory().getChannel(triggerNamePV);
    }
