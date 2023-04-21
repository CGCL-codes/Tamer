    public NonBlockingConnectedEndpoint(SocketAddress remoteAddress, IDatagramHandler appHandler, int receivePacketSize, int workerPoolSize) throws IOException {
        super(0, appHandler, receivePacketSize, workerPoolSize);
        this.remoteAddress = remoteAddress;
        getChannel().connect(remoteAddress);
        connectionOpenedTime = System.currentTimeMillis();
    }
