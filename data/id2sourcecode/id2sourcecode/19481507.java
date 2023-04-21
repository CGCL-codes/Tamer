    public void writeBytes(final byte[] bytes, final int startOffset, final int numBytes) {
        if (!isWriting) {
            throw new IllegalStateException("Calling write method on BinaryCodec open for read.");
        }
        try {
            outputStream.write(bytes, startOffset, numBytes);
        } catch (IOException e) {
            throw new RuntimeIOException(constructErrorMessage("Write error"), e);
        }
    }
