    @Override
    protected int write(NioSession session, IoBuffer buf, int length) throws Exception {
        if (buf.remaining() <= length) {
            return session.getChannel().write(buf.buf());
        }
        int oldLimit = buf.limit();
        buf.limit(buf.position() + length);
        try {
            return session.getChannel().write(buf.buf());
        } finally {
            buf.limit(oldLimit);
        }
    }
