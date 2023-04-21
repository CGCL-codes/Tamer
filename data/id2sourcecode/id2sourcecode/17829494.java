    public ChannelHandler(String name, SCLCavity cav, Controller cont) {
        cavity = cav;
        controller = cont;
        channel = ChannelFactory.defaultFactory().getChannel(name);
        channel.addConnectionListener(this);
        channel.requestConnection();
    }
