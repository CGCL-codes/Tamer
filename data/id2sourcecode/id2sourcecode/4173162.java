    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = vars.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                clear();
            }
            wasBypassed = true;
            return AUDIO_OK;
        }
        int sr = (int) buffer.getSampleRate();
        if (sr != sampleRate) {
            sampleRate = sr;
            vars.update(sr);
        }
        cacheProcessVariables();
        int len = buffer.getSampleCount();
        int nc = buffer.getChannelCount();
        for (int c = 0; c < nc; c++) {
            samples[c] = buffer.getChannel(c);
        }
        if (vars.isRMS()) {
            for (int i = 0; i < len; i++) {
                float key = 0;
                for (int c = 0; c < nc; c++) {
                    key += samples[c][i] * samples[c][i];
                }
                gain = (float) sqrt(dynamics(function(key)));
                for (int c = 0; c < nc; c++) {
                    samples[c][i] *= (gain * makeupGain) + dryGain;
                }
            }
        } else {
            float key = 0;
            for (int i = 0; i < len; i++) {
                for (int c = 0; c < nc; c++) {
                    key = max(key, abs(samples[c][i]));
                }
                gain = dynamics(function(key));
                for (int c = 0; c < nc; c++) {
                    samples[c][i] *= (gain * makeupGain) + dryGain;
                }
            }
        }
        vars.setDynamicGain(gain);
        wasBypassed = bypassed;
        return AUDIO_OK;
    }
