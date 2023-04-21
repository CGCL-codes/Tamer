    public Channel getChannel(String channel) {
        Enumeration channelEnum = channels.elements();
        while (channelEnum.hasMoreElements()) {
            Channel c = (Channel) channelEnum.nextElement();
            if (channel.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }
