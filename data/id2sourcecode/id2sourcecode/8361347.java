    @Override
    protected void fillUxUy() {
        int algorithm = MDC2D.getMotionMethod();
        channel = MDC2D.getChannelForMotionAlgorithm();
        numLocalToAverageForGlobal = chip.NUM_MOTION_PIXELS;
        switch(algorithm) {
            case MDC2D.NORMAL_OPTICFLOW:
                this.calculateMotion_gradientBased();
                this.localUxUy_temporalAverage(3);
                this.globalUxUy_averageLocal();
                this.globalUxUy_temporalAverage(temporalAveragesNum);
                break;
            case MDC2D.SRINIVASAN:
                this.setUx(zero());
                this.setUy(zero());
                this.calculateMotion_srinivasan();
                this.globalUxUy_temporalAverage(temporalAveragesNum);
                break;
            case MDC2D.LOCAL_SRINIVASAN:
                this.setUx(zero());
                this.setUy(zero());
                this.calculateMotion_localSrinivasan(4);
                this.localUxUy_temporalAverage(3);
                this.globalUxUy_averageLocal();
                this.globalUxUy_temporalAverage(temporalAveragesNum);
                break;
            case MDC2D.TIME_OF_TRAVEL:
                float thresholdContrast = thresh / 100;
                float matchContrast = match / 100;
                this.calculateMotion_timeOfTravel_absValue(thresholdContrast, matchContrast);
                this.globalUxUy_averageLocal();
                this.globalUxUy_temporalAverage(temporalAveragesNum);
                break;
            case MDC2D.RANDOM:
                this.calculateMotion_random();
                this.globalUxUy_temporalAverage(temporalAveragesNum);
                this.globalUxUy_averageLocal();
                break;
        }
    }
