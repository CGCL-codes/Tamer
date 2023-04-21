    public void read(Buffer buffer) throws java.io.IOException {
        synchronized (mBuffer) {
            if (mBuffer == null) {
                System.out.println("AVStream: error, mBuffer = null");
            } else if (readCount == writeCount) {
                System.out.println("AVStream: buffer already read");
            } else {
                buffer.setData(mBuffer.getData());
                buffer.setOffset(mBuffer.getOffset());
                buffer.setFormat(mBuffer.getFormat());
                buffer.setSequenceNumber(mBuffer.getSequenceNumber());
                buffer.setLength(mBuffer.getLength());
                buffer.setFlags(mBuffer.getFlags());
                buffer.setHeader(mBuffer.getHeader());
                buffer.setEOM(mBuffer.isEOM());
                readCount++;
            }
        }
    }
