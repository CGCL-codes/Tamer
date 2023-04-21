    public ReturnValue<IRCChannelBean> getChannel(String channelname) {
        ReturnValue<IRCChannelBean> retval = new ReturnValue<IRCChannelBean>();
        if (channelname == null) {
            setNullFlag(retval, CHANNEL);
        } else {
            IRCChannelBean channel = resolve(em, IRCChannelBean.class, channelname);
            if (channel == null) {
                retval.setValue(channel);
            } else {
                retval.addMessage(NOTFOUND, CHANNEL);
            }
        }
        return retval;
    }
