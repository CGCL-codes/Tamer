    public void createCueFromLive(float oldCueNumber, float newCueNumber, String name, String desc, long fadeUpMillis, long fadeDownMillis) {
        stopTransition();
        Hashtable<Short, Channel> channels = new Hashtable<Short, Channel>();
        Channel[] channelArray = channelValues.getChannelsForCue();
        for (Channel c : channelArray) channels.put(c.address, c);
        addCue(new ACue(newCueNumber, name, desc, fadeUpMillis, fadeDownMillis, show.getNewCueIndex(), maxChannels, channels));
        Channel[] changedChannels = channelValues.resetValues(ChannelValues.FADER_SOURCE);
        connector.updateChannels(channelValues.getChannels(changedChannels), channelValues.getChannelSources(changedChannels));
    }
