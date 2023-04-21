    public JMemoryPacket(ByteBuffer buffer) throws PeeringException {
        super(Type.POINTER);
        final int size = buffer.limit() - buffer.position();
        final JBuffer mem = getMemoryBuffer(size);
        super.peer(mem);
        transferFrom(buffer);
        header.setWirelen(size);
    }
