    public Channel getChannel(String name) {
        log.debug("get Chanel");
        synchronized (channels) {
            for (Channel cn : channels) {
                if (cn != null && cn.getChannelName().equals(name)) {
                    log.debug("return channel" + name);
                    return cn;
                }
            }
        }
        log.debug("cannel not found create a new one");
        Channel ch = in.getInstance(Channel.class);
        ch.setChannelName(name);
        this.addChannel(ch);
        return ch;
    }
