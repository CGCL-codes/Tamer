    public int processAudio(AudioBuffer buffer) {
        float buff[] = buffer.getChannel(0);
        int n = buffer.getSampleCount();
        if (steady) {
            for (int i = 0; i < n; i++) {
                phase += dphase2;
                if (phase >= twoPI) {
                    phase -= twoPI;
                }
                buff[i] += amp2 * FloatSinTable.sinFast(phase);
            }
        } else {
            double ddphase = (dphase2 - dphase1) / n;
            double ddamp = (amp2 - amp1) / n;
            for (int i = 0; i < n; i++) {
                phase += dphase1;
                dphase1 += ddphase;
                if (phase >= twoPI) {
                    phase -= twoPI;
                }
                buff[i] += amp1 * FloatSinTable.sinFast(phase);
                amp1 += ddamp;
            }
        }
        steady = true;
        active = amp2 != 0;
        return AUDIO_OK;
    }
