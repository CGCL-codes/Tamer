    @Override
    public final boolean isValidChannel(int channel) {
        return (0 <= channel && channel < this.getChannelCount());
    }
