    public int getChannelCount() {
        return this.header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
    }
