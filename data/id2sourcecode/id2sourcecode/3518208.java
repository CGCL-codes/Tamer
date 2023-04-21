    public int processAudio(AudioBuffer buffer) {
        if (controls.isInactive()) return AUDIO_OK;
        tap.setChannelFormat(buffer.getChannelFormat());
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        for (int i = 0; i < nc; i++) {
            System.arraycopy(buffer.getChannel(i), 0, tap.getChannel(i), 0, ns);
        }
        return AUDIO_OK;
    }
