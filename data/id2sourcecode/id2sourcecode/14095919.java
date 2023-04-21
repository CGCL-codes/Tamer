    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = vars.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                overSampler.clear();
                wasBypassed = true;
            }
            return AUDIO_OK;
        }
        wasBypassed = bypassed;
        int srate = (int) buffer.getSampleRate();
        if (srate != sampleRate) {
            sampleRate = srate;
            design();
        }
        int nsamples = buffer.getSampleCount();
        int nchans = buffer.getChannelCount() > 1 ? 2 : 1;
        float bias = vars.getBias();
        float gain = vars.getGain();
        float inverseGain = 1f / gain;
        if (inverseGain < 0.1f) inverseGain = 0.1f;
        float[] samples;
        float[] upSamples;
        float sample;
        DCBlocker dc;
        for (int c = 0; c < nchans; c++) {
            samples = buffer.getChannel(c);
            dc = blocker[c];
            for (int s = 0; s < nsamples; s++) {
                upSamples = overSampler.interpolate(gain * samples[s], c);
                for (int i = 0; i < upSamples.length; i++) {
                    sample = tanh(bias + upSamples[i]);
                    sample += 0.1f * sample * sample;
                    upSamples[i] = sample;
                }
                samples[s] = inverseGain * dc.block(overSampler.decimate(upSamples, c));
            }
        }
        return AUDIO_OK;
    }
