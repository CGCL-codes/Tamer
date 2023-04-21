    public String getChannelName(String channelName) {
        channelName = channelName.toUpperCase();
        for (int c = 0; c < channelNames.size(); c++) if (((String) channelNames.elementAt(c)).startsWith(channelName)) return ((String) channelNames.elementAt(c)).toUpperCase();
        return "";
    }
