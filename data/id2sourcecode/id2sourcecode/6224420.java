    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = vars.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                if (delayBuffer != null) {
                    delayBuffer.makeSilence();
                }
                wasBypassed = true;
            }
            return AUDIO_OK;
        }
        buffer.monoToStereo();
        float sampleRate = buffer.getSampleRate();
        float samplesPerMilli = sampleRate * 0.001f;
        int ns = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        float feedback = vars.getFeedback();
        float mix = vars.getMix();
        if (delayBuffer == null) {
            delayBuffer = new DelayBuffer(nc, (int) (vars.getMaxDelayMilliseconds() * samplesPerMilli), sampleRate);
        } else {
            delayBuffer.conform(buffer);
        }
        if (tappedBuffer == null) {
            tappedBuffer = new DelayBuffer(nc, ns, sampleRate);
        } else {
            tappedBuffer.conform(buffer);
            if (tappedBuffer.getSampleCount() != ns) {
                tappedBuffer.changeSampleCount(ns, false);
            }
        }
        tappedBuffer.makeSilence();
        float delayFactor = vars.getDelayFactor();
        ChannelFormat format = buffer.getChannelFormat();
        for (int c = 0; c < nc; c++) {
            int c2;
            if (format.isLeft(c)) c2 = ChannelFormat.STEREO.getLeft()[0]; else if (format.isRight(c)) c2 = ChannelFormat.STEREO.getRight()[0]; else continue;
            for (DelayTap tap : vars.getTaps(c2)) {
                float level = tap.getLevel();
                if (level < 0.001) continue;
                int delay = (int) (tap.getDelayMilliseconds() * delayFactor * samplesPerMilli);
                if (delay < ns) continue;
                delayBuffer.tap(c, tappedBuffer, delay, level);
            }
        }
        delayBuffer.append(buffer, tappedBuffer, feedback);
        for (int c = 0; c < nc; c++) {
            float[] samples = buffer.getChannel(c);
            float[] tapped = tappedBuffer.getChannel(c);
            for (int i = 0; i < ns; i++) {
                samples[i] += mix * tapped[i];
            }
        }
        wasBypassed = bypassed;
        return AUDIO_OK;
    }
