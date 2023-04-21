    public void initFromFloatSampleBuffer(FloatSampleBuffer source) {
        init(source.getChannelCount(), source.getSampleCount(), source.getSampleRate());
        for (int ch = 0; ch < getChannelCount(); ch++) {
            System.arraycopy(source.getChannel(ch), 0, getChannel(ch), 0, sampleCount);
        }
    }
