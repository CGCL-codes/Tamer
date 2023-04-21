    public float[] getChannelSources(Channel[] channels) {
        float[][] sources = channelValues.getChannelSources(channels);
        float[] result = new float[sources.length];
        for (int i = 0; i < result.length; i++) result[i] = sources[i][0];
        return result;
    }
