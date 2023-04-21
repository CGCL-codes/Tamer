    @Override
    protected int getChannel(final double energy) {
        final AbstractHist1D plotHist = (AbstractHist1D) getHistogram();
        return (int) Math.round(plotHist.getCalibration().getChannel(energy));
    }
