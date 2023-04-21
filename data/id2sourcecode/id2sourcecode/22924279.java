    protected AudioBuffer createBuffer() {
        int id = controlChain.getId();
        if (id == CHANNEL_STRIP) {
            isChannel = true;
            return mixer.getSharedBuffer();
        } else if (id == GROUP_STRIP) {
            AudioBuffer buf = mixer.createBuffer(getName());
            buf.setChannelFormat(mixer.getMainBus().getBuffer().getChannelFormat());
            return buf;
        } else if (id == MAIN_STRIP) {
            return mixer.getMainBus().getBuffer();
        }
        return mixer.getBus(getName()).getBuffer();
    }
