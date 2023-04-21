    protected JBuffer getMemoryBuffer(byte[] buffer) {
        pool.allocate(buffer.length, memory);
        memory.transferFrom(buffer);
        return memory;
    }
