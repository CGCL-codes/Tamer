    public void removeChannel(Channel cn) {
        log.debug("Remove Channel" + cn.getChannelName());
        synchronized (channels) {
            channels.remove(cn);
        }
    }
