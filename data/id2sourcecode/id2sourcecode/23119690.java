    public int transferFrom(JBuffer buffer) {
        advance(buffer.size());
        return this.storage.transferFrom(buffer);
    }
