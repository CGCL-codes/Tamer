    public BasicChromatogram(Chromatogram c) {
        this(c.getBasecalls(), c.getQualities(), c.getPeaks(), c.getChannelGroup(), c.getProperties());
    }
