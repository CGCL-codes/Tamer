    public int transferStateAndDataFrom(JBuffer buffer) {
        final int len = buffer.size();
        JBuffer b = getMemoryBuffer(len);
        b.transferFrom(buffer);
        return peerStateAndData(b, 0);
    }
