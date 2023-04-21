    public void start() {
        this.channelFactory = this.createChannelFactory();
        bootstrap = new ServerBootstrap(channelFactory);
        bootstrap.setPipelineFactory(getChannelPipelineFactory());
        bootstrap.setOption("child.tcpNoDelay", Config.get().getBoolean(this.serverName() + "child.tcpNoDelay", true));
        bootstrap.setOption("child.keepAlive", Config.get().getBoolean(this.serverName() + "child.keepAlive", true));
        Channel serverChannel = bootstrap.bind(this.getSocketAddress());
        allChannels.add(serverChannel);
        log.info("server started at:" + this.getSocketAddress());
    }
