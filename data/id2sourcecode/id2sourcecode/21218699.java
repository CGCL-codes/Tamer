    public Channel getChannel(short address) {
        Channel c = channels.get(address);
        if (c != null) return c; else return new Channel(address, (short) 0);
    }
