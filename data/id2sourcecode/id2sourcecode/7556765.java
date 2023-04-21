    private void removeAllChannels() {
        Enumeration keys = getChannels().keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Channel chan = (Channel) getChannels().get(key);
            removeChannel(chan);
        }
    }
