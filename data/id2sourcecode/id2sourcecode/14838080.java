    public Object[] getAllChannels() {
        Object[] res = new Object[getChannelCount()];
        for (int ch = 0; ch < getChannelCount(); ch++) {
            res[ch] = getChannel(ch);
        }
        return res;
    }
