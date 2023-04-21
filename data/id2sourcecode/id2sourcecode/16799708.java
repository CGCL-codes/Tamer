    protected void conformBandBuffers(AudioBuffer buf) {
        int nc = buf.getChannelCount();
        int ns = buf.getSampleCount();
        int sr = (int) buf.getSampleRate();
        if (bandBuffers == null) {
            bandBuffers = new AudioBuffer[nbands];
            for (int b = 0; b < nbands; b++) {
                bandBuffers[b] = new AudioBuffer("MultiBandCompressor band " + (1 + b), nc, ns, sr);
            }
            updateSampleRate(sr);
        } else {
            if (nchans >= nc && nsamples == ns && sampleRate == sr) return;
            for (int b = 0; b < nbands; b++) {
                AudioBuffer bbuf = bandBuffers[b];
                if (nchans < nc) {
                    for (int i = 0; i < nc - nchans; i++) {
                        bbuf.addChannel(true);
                    }
                }
                if (nsamples != ns) {
                    bbuf.changeSampleCount(ns, false);
                }
                if (sampleRate != sr) {
                    bbuf.setSampleRate(sr);
                    updateSampleRate(sr);
                }
            }
        }
        nchans = nc;
        nsamples = ns;
        sampleRate = sr;
    }
