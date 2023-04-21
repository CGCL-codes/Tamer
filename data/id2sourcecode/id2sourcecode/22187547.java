    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Chromatogram)) {
            return false;
        }
        final Chromatogram other = (Chromatogram) obj;
        return CommonUtil.similarTo(getNucleotideSequence(), other.getNucleotideSequence()) && CommonUtil.similarTo(getPeaks(), other.getPeaks()) && CommonUtil.similarTo(getChannelGroup(), other.getChannelGroup()) && CommonUtil.similarTo(getComments(), other.getComments());
    }
