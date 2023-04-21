    private static int channelIO(ReadableByteChannel readCh, WritableByteChannel writeCh, ByteBuffer buf) throws IOException {
        int originalLimit = buf.limit();
        int initialRemaining = buf.remaining();
        int ret = 0;
        while (buf.remaining() > 0) {
            try {
                int ioSize = Math.min(buf.remaining(), NIO_BUFFER_LIMIT);
                buf.limit(buf.position() + ioSize);
                ret = (readCh == null) ? writeCh.write(buf) : readCh.read(buf);
                if (ret < ioSize) {
                    break;
                }
            } finally {
                buf.limit(originalLimit);
            }
        }
        int nBytes = initialRemaining - buf.remaining();
        return (nBytes > 0) ? nBytes : ret;
    }
