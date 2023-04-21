    public Channel[] resetValues(int source) {
        IValueSet set = channelSets.getSet(source);
        Channel[] results = set.getChannels();
        set.resetValues();
        return results;
    }
