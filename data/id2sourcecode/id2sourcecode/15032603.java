    public void initialize(int pSampleSize, SourceDataLine pSourceDataLine) {
        setSpectrumAnalyserSampleSizeAndRate(pSampleSize, pSourceDataLine.getFormat().getSampleRate());
        oldVolume = new float[pSourceDataLine.getFormat().getChannels()];
    }
