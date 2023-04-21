    private ByteBuffer fetchBytes(long offset, long len) throws IOException {
        ByteBuffer buf = rf.getChannel().map(FileChannel.MapMode.READ_ONLY, offset, len);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf;
    }
