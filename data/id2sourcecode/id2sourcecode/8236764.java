    public ConnectedEndpoint(SocketAddress remoteAddress, Map<String, Object> options, int receivePacketSize, IDatagramHandler datagramHandler, Executor workerPool) throws IOException {
        super(new InetSocketAddress(0), options, datagramHandler, receivePacketSize, workerPool);
        this.remoteAddress = remoteAddress;
        getChannel().connect(remoteAddress);
    }
