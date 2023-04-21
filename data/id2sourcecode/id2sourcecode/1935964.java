    public int[] getChannelSources(Channel[] channels) {
        int[] values = new int[channels.length];
        for (int i = 0; i < channels.length; i++) values[i] = getChannelSource(channels[i].address);
        return values;
    }
