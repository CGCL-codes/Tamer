    @Override
    public int processAudio(AudioBuffer arg0) {
        float buff[] = arg0.getChannel(0);
        int nSamp = arg0.getSampleCount();
        synchronized (oscs) {
            for (Oscillator osc : oscs) {
                double re = osc.state[RE];
                double im = osc.state[IM];
                double reRe = osc.state[RERE];
                double reIm = osc.state[REIM];
                double imRe = osc.state[IMRE];
                double imIm = osc.state[IMIM];
                for (int j = 0; j < nSamp; j++) {
                    double re1 = re * reRe + im * reIm;
                    im = re * imRe + im * imIm;
                    re = re1;
                    buff[j] += (float) re;
                }
                osc.state[RE] = re;
                osc.state[IM] = im;
            }
        }
        return AUDIO_OK;
    }
