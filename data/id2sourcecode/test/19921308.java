    protected int transferFrom(ByteBuffer src, int dstOffset) {
        if (src.isDirect()) {
            return transferFromDirect(src, 0);
        } else {
            return transferFrom(src.array(), src.position(), src.limit() - src.position(), 0);
        }
    }
