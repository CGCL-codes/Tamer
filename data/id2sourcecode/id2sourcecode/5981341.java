    public static boolean readDataNonBlockingRequestCollecting(int len, SocketChannel soc, ByteBuffer bb2, RequestBuffer out) throws IOException, ConnectException {
        soc.configureBlocking(false);
        int read = 0;
        int bufferSize = len;
        if (out.requestReadByteDataRead + bufferSize > len) bufferSize = len - out.requestReadByteDataRead;
        if (bufferSize > MAX_MEMORY_BINARY_READ_BUFFER) bufferSize = MAX_MEMORY_BINARY_READ_BUFFER;
        ByteBuffer bb = ByteBuffer.wrap(new byte[bufferSize]);
        bb.rewind();
        while ((read = soc.read(bb)) > 0) {
            byte[] b = bb.array();
            bb.rewind();
            out.write(b, 0, read);
            out.requestReadByteDataRead += read;
            if (out.requestReadByteDataRead >= len) {
                out.requestReadByteDataRead = 0;
                return true;
            }
            if (out.requestReadByteDataRead + bufferSize > len) {
                bufferSize = len - out.requestReadByteDataRead;
                if (bufferSize > MAX_MEMORY_BINARY_READ_BUFFER) bufferSize = MAX_MEMORY_BINARY_READ_BUFFER;
                bb = ByteBuffer.wrap(new byte[bufferSize]);
            }
        }
        soc.configureBlocking(true);
        if (read < 0) throw new ConnectException("quite");
        return false;
    }
