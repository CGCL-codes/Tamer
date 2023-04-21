    public JMemoryPacket(JMemoryPacket packet) {
        super(Type.POINTER);
        transferFrom(packet);
    }
