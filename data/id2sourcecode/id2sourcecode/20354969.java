    public int transferHeaderAndDataFrom(PcapHeader header, ByteBuffer buffer) {
        final int len = buffer.limit() - buffer.position();
        JBuffer b = getMemoryBuffer(header.size() + len);
        int o = header.transferTo(b, 0);
        o += b.transferFrom(buffer, o);
        peerHeaderAndData(b);
        return o;
    }
