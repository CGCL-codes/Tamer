    public PVOutput(String n) {
        super(n, 1, 0);
        ch = Utilities.getChannelFactory().getChannel(n);
        ch.connect();
    }
