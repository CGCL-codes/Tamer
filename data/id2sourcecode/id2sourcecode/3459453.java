    public int transferFrom(ReadableByteChannel channel) throws IOException {
        ensureCapacity(buffer.position() + 1);
        return channel.read(buffer);
    }
