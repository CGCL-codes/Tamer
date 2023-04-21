    @Override
    public boolean SoundIsPlaying(int handle) {
        int c = getChannelFromHandle(handle);
        return (c != -2 && channels[c] == null);
    }
