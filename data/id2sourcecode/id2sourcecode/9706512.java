    public SocketWriter(Socket socket, int limit) {
        this.builder = new SegmentBuilder(limit);
        this.channel = socket.getChannel();
    }
