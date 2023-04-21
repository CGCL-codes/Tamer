    @Override
    protected void destroy(NioSession session) throws Exception {
        ByteChannel ch = session.getChannel();
        SelectionKey key = session.getSelectionKey();
        if (key != null) {
            key.cancel();
        }
        ch.close();
    }
