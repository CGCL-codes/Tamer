    private int getChannelMin(final int channel) {
        int rval = channel;
        if ((channel == 0) && (options.isIgnoreChZero())) {
            rval = 1;
        }
        return rval;
    }
