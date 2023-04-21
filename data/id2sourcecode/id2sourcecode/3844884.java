    public int read(ByteBuffer b) throws IOException {
        return socket.getChannel().read(b);
    }
