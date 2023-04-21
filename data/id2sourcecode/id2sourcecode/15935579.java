    public int processAudio(AudioBuffer buffer) {
        int n = buffer.getSampleCount();
        int nch = buffer.getChannelCount();
        for (int ch = 0; ch < nch; ch++) {
            float[] buff = buffer.getChannel(ch);
            for (int i = 0; i < n; i++) {
                if (Math.abs(buff[i]) > monitVal) monitVal = Math.abs(buff[i]);
            }
        }
        return AUDIO_OK;
    }
