    public BasicChromatogram(Chromatogram c) {
        this(c.getId(), c.getNucleotideSequence(), c.getQualities(), c.getPeaks(), c.getChannelGroup(), c.getComments());
    }
