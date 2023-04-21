    public int readBytesOrFewer(final byte[] buffer, final int offset, final int length) {
        if (isWriting) {
            throw new IllegalStateException("Calling read method on BinaryCodec open for write.");
        }
        try {
            return inputStream.read(buffer, offset, length);
        } catch (IOException e) {
            throw new RuntimeIOException(constructErrorMessage("Read error"), e);
        }
    }
