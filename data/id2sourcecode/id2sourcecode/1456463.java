    public ReturnValue<IRCChannelBean> getChannelWithJoins(String channelname) {
        ReturnValue<IRCChannelBean> retval = new ReturnValue<IRCChannelBean>();
        if (channelname == null) {
            setNullFlag(retval, CHANNEL);
        } else {
            IRCChannelBean channel = resolve(em, IRCChannelBean.class, channelname);
            if (channel != null) {
                channel.getJoins().size();
                retval.setValue(channel);
            } else {
                retval.addMessage(NOTFOUND, CHANNEL);
            }
        }
        return retval;
    }
