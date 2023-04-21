    protected NIOSocket(Socket s) {
        channel = s.getChannel();
        socket = channel.socket();
        remoteSocketAddress = s.getRemoteSocketAddress();
        initIncomingSocket();
        setInitialReader();
        setInitialWriter();
        NIODispatcher.instance().register(channel, this);
    }
