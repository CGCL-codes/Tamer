    public synchronized Channel getChannel(String name, boolean force) {
        name = name.trim().toLowerCase();
        Channel chan = (Channel) getChannels().get(name);
        if (chan == null && force) {
            chan = new Channel(name, this);
            chan.setConnected(true);
            addChannel(chan);
        }
        return chan;
    }
