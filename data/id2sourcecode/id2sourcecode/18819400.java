    protected void setChannels() {
        String name1 = BPM1Name + ":phaseAvg";
        String name2 = BPM2Name + ":phaseAvg";
        Channel channel1 = ChannelFactory.defaultFactory().getChannel(name1);
        Channel channel2 = ChannelFactory.defaultFactory().getChannel(name2);
        setChannels(channel1, channel2);
    }
