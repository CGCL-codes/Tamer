    public String[] getChannelsList() {
        String chans[] = new String[getChannelsCount()];
        for (int i = 0; i < channels.size(); i++) {
            chans[i] = new String(((OJIRCChannel) channels.elementAt(i)).getChannelName());
        }
        return chans;
    }
