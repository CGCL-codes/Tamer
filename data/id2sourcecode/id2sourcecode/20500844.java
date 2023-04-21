    protected long transferChunk(ReadableByteChannel readable, WritableByteChannel writable, long transferred, long remaining) throws IOException {
        return fileChannel.transferFrom(readable, offset + transferred, Math.min(1024, remaining));
    }
