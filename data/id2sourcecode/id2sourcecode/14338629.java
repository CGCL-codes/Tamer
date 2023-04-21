    public long transferFrom(ReadableByteChannel source) throws IOException, BufferOverflowException, ClosedChannelException {
        return transferFrom(source, TRANSFER_BYTE_BUFFER_MAX_MAP_SIZE);
    }
