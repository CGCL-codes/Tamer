    public BasicChromatogramBuilder(Chromatogram copy) {
        this(copy.getId(), copy.getNucleotideSequence(), ShortSymbol.toArray(copy.getPeaks().getData().asList()), copy.getChannelGroup(), copy.getComments());
    }
