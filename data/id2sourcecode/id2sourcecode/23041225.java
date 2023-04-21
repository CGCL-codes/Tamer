    public int processAudio(AudioBuffer buffer) {
        if (vars.isBypassed()) return AUDIO_OK;
        int sr = (int) buffer.getSampleRate();
        int ns = buffer.getSampleCount();
        float[] samples = buffer.getChannel(0);
        if (sr != sampleRate) {
            dmin = 440f / (sr / 2f);
            dmax = 1600f / (sr / 2f);
            sampleRate = sr;
        }
        int n = vars.getStages();
        float depth = vars.getDepth();
        float fb = vars.getFeedback();
        float _lfoInc = 2 * (float) Math.PI * (vars.getRate() / sampleRate);
        for (int i = 0; i < ns; i++) {
            float d = dmin + (dmax - dmin) * ((FastMath.sin(lfoPhase) + 1f) * 0.5f);
            lfoPhase += _lfoInc;
            if (lfoPhase >= Math.PI) lfoPhase -= Math.PI * 2;
            a1 = (1f - d) / (1f + d);
            float y = samples[i] + zm1 * fb;
            for (int a = 0; a < n; a++) {
                y = allpass[a].update(y);
            }
            zm1 = zeroDenorm(y);
            samples[i] += zm1 * depth;
        }
        return AUDIO_OK;
    }
