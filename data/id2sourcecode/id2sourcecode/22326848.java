    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = vars.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                if (delayBuffer != null) delayBuffer.makeSilence();
                wasBypassed = true;
            }
            return AUDIO_OK;
        }
        wasBypassed = bypassed;
        int sr = (int) buffer.getSampleRate();
        if (sampleRate != sr) {
            sampleRate = sr;
            delayBuffer = new DelayBuffer(buffer.getChannelCount(), msToSamples(maxDelayMillis, sampleRate), sampleRate);
            staticDelay = (int) (delayBuffer.msToSamples(maxDelayMillis / 2f));
            sampleRateChanged();
        }
        delayBuffer.conform(buffer);
        cacheProcessVariables();
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        for (int c = 0; c < nc; c++) {
            samples[c] = buffer.getChannel(c);
        }
        for (int s = 0; s < ns; s++) {
            float delay = staticDelay * (1f + modulation());
            for (int ch = 0; ch < nc; ch++) {
                delayBuffer.append(ch, samples[ch][s]);
                samples[ch][s] = delayBuffer.outA(ch, delay);
            }
            delayBuffer.nudge(1);
        }
        return AUDIO_OK;
    }
