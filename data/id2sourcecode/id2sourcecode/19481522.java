    public byte readByte() {
        if (isWriting) {
            throw new IllegalStateException("Calling read method on BinaryCodec open for write.");
        }
        try {
            final int ret = inputStream.read();
            if (ret == -1) {
                throw new RuntimeEOFException(constructErrorMessage("Premature EOF"));
            }
            return (byte) ret;
        } catch (IOException e) {
            throw new RuntimeIOException(constructErrorMessage("Read error"), e);
        }
    }
