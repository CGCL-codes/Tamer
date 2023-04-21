    public SocketTransport(Socket socket, Reactor reactor, int limit, int queue) throws IOException {
        this.writer = new SocketController(socket, reactor, limit);
        this.builder = new PacketBuilder(queue);
        this.channel = socket.getChannel();
        this.socket = socket;
    }
