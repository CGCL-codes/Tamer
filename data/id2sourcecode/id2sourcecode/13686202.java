    @Override
    public void StopSound(int handle) {
        int hnd = getChannelFromHandle(handle);
        if (hnd >= 0) {
            channels[hnd] = false;
            this.channelhandles[hnd] = IDLE_HANDLE;
            MixMessage m = new MixMessage();
            m.channel = hnd;
            m.stop = true;
            MIXSRV.submitMixMessage(m);
        }
    }
