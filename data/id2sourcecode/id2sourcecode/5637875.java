    public int processAudio(AudioBuffer buffer) {
        if (controls.isBypassed()) return AUDIO_OK;
        buffer.setChannelFormat(ChannelFormat.MONO);
        int ns = buffer.getSampleCount();
        float[] samples = buffer.getChannel(0);
        for (int i = 0; i < ns; i++) {
            samples[i] = noise() * 0.1f;
        }
        return AUDIO_OK;
    }
