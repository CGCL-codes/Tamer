    public void flush(Session session, ByteBuffer buf) throws IOException {
        ByteBuffer bak = buf.duplicate();
        int num = session.getChannel().write(buf);
        if (num != buf.limit()) {
            while (buf.hasRemaining()) {
                session.getChannel().write(buf);
            }
        }
        fireMessageFlush(session, bak);
    }
