    public int processAudio(AudioBuffer buffer) {
        if (controls.isBypassed()) return AUDIO_OK;
        float trim = controls.getTrim();
        int nc = buffer.getChannelCount();
        int ns = buffer.getSampleCount();
        float[] samples;
        for (int c = 0; c < nc; c++) {
            samples = buffer.getChannel(c);
            for (int s = 0; s < ns; s++) {
                samples[s] *= trim;
            }
        }
        return AUDIO_OK;
    }
