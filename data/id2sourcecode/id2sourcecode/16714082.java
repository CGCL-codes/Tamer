    public int processAudio(AudioBuffer buffer) {
        if (controls.isBypassed()) return AUDIO_OK;
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        int sc = ns * nc;
        int nans = 0;
        float[] samples;
        float f;
        for (int c = 0; c < nc; c++) {
            samples = buffer.getChannel(c);
            for (int s = 0; s < ns; s++) {
                f = samples[s];
                if (f == f) continue;
                samples[s] = 0;
                nans++;
            }
        }
        controls.setNaNFactor((float) nans / sc);
        return AUDIO_OK;
    }
