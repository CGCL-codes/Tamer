    private void doRead(SelectionKey key) throws IOException {
        Session session = (Session) key.attachment();
        SocketChannel channel = session.getChannel();
        ByteBuffer buf = bufferPool.obtain();
        int k = channel.read(buf);
        if (k >= 0) {
            fireMessageReceived(session, buf);
        } else {
            close(channel);
            fireSessionClosed(session);
        }
        bufferPool.release(buf);
    }
