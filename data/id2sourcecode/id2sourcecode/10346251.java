    public int getChannelMode() {
        return ((header[3] & 0xC0) >> 6);
    }
