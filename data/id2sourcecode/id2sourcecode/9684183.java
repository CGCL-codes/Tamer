    public int getChannels() {
        if (getMode() != MPEG_MODE_MONO) return 2;
        return 1;
    }
