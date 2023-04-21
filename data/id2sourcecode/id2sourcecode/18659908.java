    public JMemoryPacket(JBuffer buffer) {
        super(POINTER);
        header.setWirelen(buffer.size());
        final int len = buffer.size();
        JBuffer b = getMemoryBuffer(len);
        b.transferFrom(buffer);
        peer(b, 0, len);
        header.setWirelen(len);
    }
