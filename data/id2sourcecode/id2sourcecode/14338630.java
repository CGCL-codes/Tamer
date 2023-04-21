    public long transferFrom(ReadableByteChannel source, int chunkSize) throws IOException, BufferOverflowException, ClosedChannelException {
        return transfer(source, this, chunkSize);
    }
