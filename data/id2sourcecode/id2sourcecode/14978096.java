    @Override
    public int write(ByteBuffer src) throws IOException {
        if (!isOpen()) throw new EOFException("No current channel to write bytes to");
        if (null == src) return 0;
        return getChannel().write(src);
    }
