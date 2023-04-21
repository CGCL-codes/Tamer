    public int getChannelIndex(String channelName) {
        channelName = channelName.toUpperCase();
        for (int c = 0; c < channelNames.size(); c++) if (((String) channelNames.elementAt(c)).startsWith(channelName)) return c;
        return -1;
    }
