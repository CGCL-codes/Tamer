    @Override
    public void UpdateSoundParams(int handle, int vol, int sep, int pitch) {
        int i = getChannelFromHandle(handle);
        if (i != BUSY_HANDLE) {
            channels[i].setVolume(vol);
            channels[i].setPitch(pitch);
            channels[i].setPanning(sep);
        }
    }
