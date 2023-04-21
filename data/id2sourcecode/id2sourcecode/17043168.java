    public int processAudio(AudioBuffer buffer) {
        if (vars.isBypassed()) return AUDIO_OK;
        precision = vars.getPrecision();
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        for (int c = 0; c < nc; c++) {
            float[] samples = buffer.getChannel(c);
            for (int s = 0; s < ns; s++) {
                samples[s] = crush(samples[s]);
            }
        }
        return AUDIO_OK;
    }
