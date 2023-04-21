    public void getChannelSamples(int channel, double[] interleavedSamples, double[] channelSamples) {
        int nbChannels = getFormat().getChannels();
        for (int i = 0; i < channelSamples.length; i++) {
            channelSamples[i] = interleavedSamples[nbChannels * i + channel];
        }
    }
