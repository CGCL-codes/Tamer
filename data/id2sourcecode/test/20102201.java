    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = vars.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                stack.clear();
                overSampler.clear();
                wasBypassed = true;
            }
            return AUDIO_OK;
        }
        wasBypassed = bypassed;
        if (buffer.getChannelCount() > 1) {
            buffer.convertTo(ChannelFormat.MONO);
        }
        int srate = (int) buffer.getSampleRate();
        if (srate != sampleRate) {
            sampleRate = srate;
            design();
            stack.updateCoefficients(vars.setSampleRate(R * sampleRate));
        } else if (vars.hasChanged()) {
            stack.updateCoefficients(vars.getCoefficients());
        }
        float bias = vars.getBias();
        float gain1 = vars.getGain1();
        float inverseGain1 = 1f / gain1;
        if (inverseGain1 < 0.1f) inverseGain1 = 0.1f;
        float gain2 = vars.getGain2();
        float inverseGain2 = 1f / gain2;
        if (inverseGain2 < 0.1f) inverseGain2 = 0.1f;
        inverseGain2 *= vars.getMaster();
        float[] upSamples;
        float sample;
        float[] samples = buffer.getChannel(0);
        int nsamples = buffer.getSampleCount();
        for (int s = 0; s < nsamples; s++) {
            upSamples = overSampler.interpolate(gain1 * samples[s], 0);
            for (int i = 0; i < upSamples.length; i++) {
                sample = tanh(bias + upSamples[i]);
                sample += 0.1f * sample * sample;
                sample = stack.filter(inverseGain1 * dc1.block(sample));
                sample = tanh(sample * gain2);
                sample += 0.1f * sample * sample;
                upSamples[i] = sample;
            }
            samples[s] = inverseGain2 * dc2.block(overSampler.decimate(upSamples, 0));
        }
        return AUDIO_OK;
    }
