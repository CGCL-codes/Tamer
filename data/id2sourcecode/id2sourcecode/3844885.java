    public int write(ByteBuffer b) throws IOException {
        return socket.getChannel().write(b);
    }
