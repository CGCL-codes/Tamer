    public Channel getChannel(long id) {
        log.debug("get Chanel");
        synchronized (channels) {
            for (Channel cn : channels) {
                if (cn.getChannelId() == id) {
                    log.debug("return channel" + id);
                    return cn;
                }
            }
        }
        log.debug("cannel not found create a new one");
        Channel ch = in.getInstance(Channel.class);
        ch.setChannelName(id + "");
        ch.setChannelId(id);
        this.addChannel(ch);
        return ch;
    }
