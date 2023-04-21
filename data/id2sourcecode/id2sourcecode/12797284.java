    @Override
    public void UpdateSoundParams(int handle, int vol, int sep, int pitch) {
        int i = getChannelFromHandle(handle);
        if (i != BUSY_HANDLE) {
            setVolume(i, vol);
            setPanning(i, sep);
        }
    }
