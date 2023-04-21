    public void mixDownChannels() {
        float[] firstChannel = getChannel(0);
        int sampleCount = getSampleCount();
        int channelCount = getChannelCount();
        for (int ch = channelCount - 1; ch > 0; ch--) {
            float[] thisChannel = getChannel(ch);
            for (int i = 0; i < sampleCount; i++) {
                firstChannel[i] += thisChannel[i];
            }
            removeChannel(ch);
        }
    }
