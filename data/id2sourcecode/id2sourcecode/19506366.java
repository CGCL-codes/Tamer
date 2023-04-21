    @Override
    protected int postProcessAudio(AudioBuffer buffer, int ret) {
        if (formantVars.isBypassed()) return 0;
        if (ret == AudioProcess.AUDIO_SILENCE) return 0;
        formantFilter.update();
        float[] samples = buffer.getChannel(0);
        int nsamples = buffer.getSampleCount();
        for (int i = 0; i < nsamples; i++) {
            samples[i] = formantFilter.filter(samples[i]);
        }
        return 0;
    }
