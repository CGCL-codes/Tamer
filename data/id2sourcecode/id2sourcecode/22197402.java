    public int map(RandomAccessFile file, long offset, int addr, int len, boolean read, boolean write, boolean exec) throws MemoryMapException {
        return mem.map(file, offset, addr, len, read, write, exec);
    }
