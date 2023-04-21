    public JMemory(ByteBuffer peer) {
        this(peer.limit() - peer.position());
        transferFrom(peer);
    }
