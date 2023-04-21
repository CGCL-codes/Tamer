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
        phaseQuad = vars.isPhaseQuadrature();
        if (phaseQuad) buffer.monoToStereo();
        float sampleRate = buffer.getSampleRate();
        if (delayBuffer == null) {
            delayBuffer = new DelayBuffer(buffer.getChannelCount(), msToSamples(vars.getMaxDelayMilliseconds(), sampleRate), sampleRate);
        }
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        float depth = vars.getDepth();
        float feedback = vars.getFeedback();
        float dry = vars.getDry();
        float wet = vars.getWet();
        int staticDelay = (int) (delayBuffer.msToSamples(vars.getDelayMilliseconds()));
        delayBuffer.conform(buffer);
        ChannelFormat f = buffer.getChannelFormat();
        if (format != f) {
            format = f;
            buildModulatorMap(buffer);
        }
        float timeDelta = 1 / sampleRate;
        float depth2 = staticDelay * depth;
        for (int c = 0; c < nc; c++) {
            samples[c] = buffer.getChannel(c);
        }
        for (int s = 0; s < ns; s++) {
            incrementModulators(timeDelta);
            for (int ch = 0; ch < nc; ch++) {
                float in = samples[ch][s];
                delayBuffer.append(ch, zeroDenorm(in - feedback * delayBuffer.outU(ch, staticDelay)));
                samples[ch][s] = dry * in + wet * delayBuffer.outA(ch, staticDelay + modulation(ch) * depth2);
            }
            delayBuffer.nudge(1);
        }
        return AUDIO_OK;
    }
