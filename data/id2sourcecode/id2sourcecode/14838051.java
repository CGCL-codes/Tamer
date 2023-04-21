    protected void init(int channelCount, int sampleCount, float sampleRate, boolean lazy) {
        if (channelCount < 0 || sampleCount < 0) {
            throw new IllegalArgumentException("Invalid parameters in initialization of FloatSampleBuffer.");
        }
        setSampleRate(sampleRate);
        if (getSampleCount() != sampleCount || getChannelCount() != channelCount) {
            createChannels(channelCount, sampleCount, lazy);
        }
    }
