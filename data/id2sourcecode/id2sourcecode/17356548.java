    public static ByteBuffer[] getWritableBuffers(ByteQueue b, ByteBuffer rc[]) {
        b.ensureBacked();
        if (b.readPosition == 0) {
            rc[0] = ByteBuffer.wrap(b.data, b.writePosition, b.data.length - b.writePosition);
            rc[1] = null;
        } else if (b.writePosition >= b.data.length) {
            rc[0] = ByteBuffer.wrap(b.data, b.writePosition - b.data.length, b.readPosition);
            rc[1] = null;
        } else {
            int half = b.data.length * 2 - b.writePosition;
            rc[0] = ByteBuffer.wrap(b.data, b.writePosition, half);
            rc[1] = ByteBuffer.wrap(b.data, 0, b.readPosition);
        }
        return rc;
    }
