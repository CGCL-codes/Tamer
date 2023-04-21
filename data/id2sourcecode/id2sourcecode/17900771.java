    @Override
    protected int read(NioSession session, IoBuffer buf) throws Exception {
        ByteChannel channel = session.getChannel();
        return session.getChannel().read(buf.buf());
    }
