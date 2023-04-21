    public void makeSilence(int channel) {
        float[] samples = getChannel(0);
        for (int i = 0; i < getSampleCount(); i++) {
            samples[i] = 0.0f;
        }
    }
