    public int transferStateAndDataFrom(ByteBuffer buffer) {
        final int len = buffer.limit() - buffer.position();
        JBuffer b = getMemoryBuffer(len);
        b.transferFrom(buffer, 0);
        return peerStateAndData(b, 0);
    }
