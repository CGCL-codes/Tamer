    public void start() {
        if (cf == null) {
            cf = ChannelFactory.defaultFactory();
        }
        chan = cf.getChannel(getName());
        chan.addConnectionListener(this);
        chan.requestConnection();
    }
