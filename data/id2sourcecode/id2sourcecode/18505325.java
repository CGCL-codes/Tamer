    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Chromatogram)) {
            return false;
        }
        final Chromatogram other = (Chromatogram) obj;
        return CommonUtil.similarTo(getBasecalls(), other.getBasecalls()) && CommonUtil.similarTo(getPeaks(), other.getPeaks()) && CommonUtil.similarTo(getChannelGroup(), other.getChannelGroup()) && CommonUtil.similarTo(getProperties(), other.getProperties());
    }
