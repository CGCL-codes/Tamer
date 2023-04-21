    public BayeuxChannel getChannel(String name) {
        BayeuxChannel bayeuxChannel = null;
        Channel channel = bayeux.getChannel(name, false);
        if (channel != null) {
            bayeuxChannel = new BayeuxChannel(this, channel);
        }
        return bayeuxChannel;
    }
