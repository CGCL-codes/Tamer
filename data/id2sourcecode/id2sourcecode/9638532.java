    private int getChannelMax(final int channel, final int sizeIn) {
        int rval = channel;
        if (((channel == sizeIn - 1)) && (options.isIgnoreChFull())) {
            rval = sizeIn - 2;
        }
        return rval;
    }
