    public String[] getChannelNames() {
        if (channelNames.size() == 0) return null;
        return CMParms.toStringArray(channelNames);
    }
