    private int sendMessage(IHeader header, int contentLength, ReadableByteChannel bodyChannel) throws IOException {
        int written = 0;
        IWriteableChannel bodyHandle = sendMessageHeader(header, contentLength);
        written += header.toString().getBytes(getDefaultEncoding()).length;
        written += bodyHandle.transferFrom(bodyChannel);
        bodyHandle.close();
        return written;
    }
