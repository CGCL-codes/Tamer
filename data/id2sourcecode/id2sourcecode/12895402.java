    public float[][] getChannelSources(Channel[] channels) {
        float[][] sources = new float[channels.length][];
        for (int i = 0; i < channels.length; i++) sources[i] = getChannelSource(channels[i]);
        return sources;
    }
