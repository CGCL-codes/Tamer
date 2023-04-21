    public int processAudio(AudioBuffer buffer) {
        buffer.setMetaInfo(info);
        buffer.setChannelFormat(ChannelFormat.MONO);
        if (isComplete()) {
            buffer.makeSilence();
            return AudioProcess.AUDIO_SILENCE;
        }
        sampleCount = buffer.getSampleCount();
        int sr = (int) buffer.getSampleRate();
        if (sr != sampleRate) {
            setSampleRate(sr);
            sampleRate = sr;
        }
        if (gliding) glide();
        update(frequency);
        float[] samples = buffer.getChannel(0);
        int nsamples = buffer.getSampleCount();
        for (int i = 0; i < nsamples; i++) {
            samples[i] += getSample();
        }
        return AudioProcess.AUDIO_OK;
    }
