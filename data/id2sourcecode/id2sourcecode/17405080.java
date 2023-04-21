    public DecimatedWaveTrail(AudioTrail fullScale, int model, int[] decimations) throws IOException {
        super();
        switch(model) {
            case MODEL_HALFWAVE_PEAKRMS:
                modelChannels = 4;
                decimator = new HalfPeakRMSDecimator();
                break;
            case MODEL_MEDIAN:
                modelChannels = 1;
                decimator = new MedianDecimator();
                break;
            case MODEL_FULLWAVE_PEAKRMS:
                modelChannels = 3;
                decimator = new FullPeakRMSDecimator();
                break;
            default:
                throw new IllegalArgumentException("Model " + model);
        }
        fullChannels = fullScale.getChannelNum();
        decimChannels = fullChannels * modelChannels;
        this.model = model;
        SUBNUM = decimations.length;
        this.decimHelps = new DecimationHelp[SUBNUM];
        for (int i = 0; i < SUBNUM; i++) {
            this.decimHelps[i] = new DecimationHelp(fullScale.getRate(), decimations[i]);
        }
        MAXSHIFT = decimations[SUBNUM - 1];
        MAXCOARSE = 1 << MAXSHIFT;
        MAXMASK = -MAXCOARSE;
        MAXCEILADD = MAXCOARSE - 1;
        tmpBufSize = Math.max(4096, MAXCOARSE << 1);
        tmpBufSize2 = SUBNUM > 0 ? Math.max(4096, tmpBufSize >> decimations[0]) : tmpBufSize;
        setRate(fullScale.getRate());
        this.fullScale = fullScale;
        fullScale.addDependant(this);
        addAllDepAsync();
    }
