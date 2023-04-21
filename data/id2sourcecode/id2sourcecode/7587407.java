    public String getChannelMask(int i) {
        if ((i >= 0) && (i < channelMasks.size())) return (String) channelMasks.elementAt(i);
        return "";
    }
