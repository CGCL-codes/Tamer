    public long transferFrom(FileChannel source) throws IOException, BufferOverflowException {
        return transferFrom((ReadableByteChannel) source);
    }
