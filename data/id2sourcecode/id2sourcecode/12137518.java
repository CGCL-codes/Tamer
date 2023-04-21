    public AbstractIRCChannel[] getChannels() {
        Collection<AbstractIRCChannel> c = channels.values();
        return c.toArray(new AbstractIRCChannel[c.size()]);
    }
