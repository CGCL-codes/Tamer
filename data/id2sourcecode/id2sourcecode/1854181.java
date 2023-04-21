    private short getShort(FileInputStream fis) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.order(ByteOrder.BIG_ENDIAN);
        fis.getChannel().read(buf);
        buf.rewind();
        return (buf.getShort());
    }
