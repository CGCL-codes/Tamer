    public void addChannel(Channel cn) {
        log.debug("Add Channel: " + cn.getChannelName());
        cn.setChannelId(getNextChannelId());
        synchronized (channels) {
            channels.add(cn);
        }
    }
