    @Override
    public int read() throws IOException {
        if (!readBuffer.isReadAvailable()) if (writeBuffer == null) return -1; else if (!readBuffer.isWriteAvailable()) readBuffer = readBuffer.next; else {
            if (!nextChunk(out)) writeBuffer = null;
            return read();
        }
        return readBuffer.bytes[readBuffer.readIndex++];
    }
