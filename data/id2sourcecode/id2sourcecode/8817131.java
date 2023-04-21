    @Override
    public void StopSound(int handle) {
        int hnd = getChannelFromHandle(handle);
        if (hnd >= 0) {
            channels[hnd] = null;
            p_channels[hnd] = 0;
            this.channelhandles[hnd] = IDLE_HANDLE;
        }
    }
