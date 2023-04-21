    public static ByteBuffer[] getReadableBuffers(ByteQueue b, ByteBuffer[] rc) {
        b.ensureBacked();
        if (b.writePosition <= b.data.length) {
            rc[0] = ByteBuffer.wrap(b.data, b.readPosition, b.writePosition - b.readPosition);
            rc[1] = null;
        } else {
            rc[0] = ByteBuffer.wrap(b.data, b.readPosition, b.data.length - b.readPosition);
            rc[1] = ByteBuffer.wrap(b.data, 0, b.writePosition - b.data.length);
            ;
        }
        return rc;
    }
