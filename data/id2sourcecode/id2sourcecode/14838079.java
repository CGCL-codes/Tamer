    public float[] getChannel(int channel) {
        if (channel < 0 || channel >= getChannelCount()) {
            throw new IllegalArgumentException("FloatSampleBuffer: invalid channel number.");
        }
        return (float[]) channels.get(channel);
    }
