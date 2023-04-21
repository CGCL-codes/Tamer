    private void regRead(Session session) {
        SocketChannel channel = session.getChannel();
        try {
            channel.register(selector, SelectionKey.OP_READ, session);
            sessions.offer(session);
            fireSessionOpened(session);
        } catch (IOException e) {
            logger.error("Fail to register OP_READ!", e);
            close(channel);
            fireSessionClosed(session);
        }
    }
