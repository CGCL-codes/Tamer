    public final void init() throws java.lang.Exception {
        if (getNFilters() == 0) {
            throw new IllegalArgumentException(getName() + " too few filters.");
        } else if (getNFrames() == 0) {
            throw new IllegalArgumentException(getName() + " too few frames.");
        }
        String chan = "c" + getChannel();
        filters = new TestFilter[getNFilters()];
        filters[0] = createFilter(0, src.getSourceName(), chan);
        chan = src.getSourceName() + "_" + chan;
        for (int idx = 1; idx < getNFilters(); ++idx) {
            filters[idx] = createFilter(idx, filters[idx - 1].getSourceName(), chan);
            chan = filters[idx - 1].getSourceName() + "_" + chan;
        }
        setOutChan(filters[getNFilters() - 1].getSourceName() + "/" + chan);
        if (debug) {
            System.err.println(getName() + " output " + getOutChan());
        }
    }
