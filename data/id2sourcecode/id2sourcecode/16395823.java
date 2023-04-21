    private int sendChunkedMessage(IHeader header, ReadableByteChannel bodyChannel) throws IOException {
        int written = 0;
        int chunkSize = getSendBufferSize() - (int) (getSendBufferSize() * 0.1);
        IWriteableChannel bodyHandle = sendChunkedMessageHeader(header, chunkSize);
        written += header.toString().getBytes(getDefaultEncoding()).length;
        written += bodyHandle.transferFrom(bodyChannel);
        bodyHandle.close();
        return written;
    }
