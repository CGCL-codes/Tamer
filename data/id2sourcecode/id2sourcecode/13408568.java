    private void doDataTest(String testString, SSLEngine srcEngine, SSLEngine dstEngine, ByteBuffer writeBuf, ByteBuffer readBuf) throws SSLException {
        ByteBuffer data = ByteBuffer.wrap(testString.getBytes());
        SSLEngineResult result = srcEngine.wrap(data, writeBuf);
        if (result.getStatus() != Status.OK) throw new IllegalStateException("Can't wrap data: " + result);
        if (data.hasRemaining()) throw new IllegalStateException("Didn't wrap all data (" + testString + "): " + data);
        writeBuf.flip();
        result = dstEngine.unwrap(writeBuf, readBuf);
        if (result.getStatus() != Status.OK) throw new IllegalStateException("Can't unwrap data: " + result);
        if (writeBuf.hasRemaining()) throw new IllegalStateException("Didn't unwrap all data!  readIn: " + writeBuf + ", made: " + readBuf);
        String read = new String(readBuf.array(), 0, readBuf.position());
        if (!testString.equals(read)) throw new IllegalStateException("Wrong data read!  Wanted: " + testString + ", was: " + read);
        readBuf.clear();
        writeBuf.clear();
    }
