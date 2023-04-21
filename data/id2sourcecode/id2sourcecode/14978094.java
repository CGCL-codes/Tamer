    @Override
    public int read(final ByteBuffer dst) throws IOException {
        if (null == dst) throw new IOException("No " + ByteBuffer.class.getName() + " instance to read into");
        if (!isOpen()) throw new EOFException("No channel to read " + ByteBuffer.class.getName() + " data");
        return getChannel().read(dst);
    }
