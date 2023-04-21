    public String getChannelName(int i) {
        if ((i >= 0) && (i < channelNames.size())) return (String) channelNames.elementAt(i);
        return "";
    }
