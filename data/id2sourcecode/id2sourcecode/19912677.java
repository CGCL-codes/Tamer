    public Handshake(Transport transport, Negotiator negotiator, int size) {
        this.output = ByteBuffer.allocate(size);
        this.input = ByteBuffer.allocate(size);
        this.channel = transport.getChannel();
        this.engine = transport.getEngine();
        this.empty = ByteBuffer.allocate(0);
        this.negotiator = negotiator;
        this.transport = transport;
    }
