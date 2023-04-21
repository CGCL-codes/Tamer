    public void start() {
        Channel channel = bind(new InetSocketAddress(getPort()));
        getChannelGroup().add(channel);
    }
