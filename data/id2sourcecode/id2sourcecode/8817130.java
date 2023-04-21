    protected int getChannelFromHandle(int handle) {
        for (int i = 0; i < numChannels; i++) {
            if (channelhandles[i] == handle) return i;
        }
        return BUSY_HANDLE;
    }
