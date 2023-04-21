    public void send(ByteBuffer bb) throws IOException {
        logger.finest("Sending to " + socket + " (user " + getId() + "):\n" + Utils.hexaString(bb.array(), true));
        SocketChannel sc = socket.getChannel();
        while (bb.remaining() > 0) {
            sc.write(bb);
        }
    }
