    public int transferFrom(ByteBuffer src, int dstOffset) {
        return this.storage.transferFrom(src, (int) (dstOffset - leftSequence));
    }
