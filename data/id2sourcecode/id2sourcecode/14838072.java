    public void expandChannel(int targetChannelCount) {
        if (getChannelCount() != 1) {
            throw new IllegalArgumentException("FloatSampleBuffer: can only expand channels for mono signals.");
        }
        for (int ch = 1; ch < targetChannelCount; ch++) {
            addChannel(false);
            copyChannel(0, ch);
        }
    }
