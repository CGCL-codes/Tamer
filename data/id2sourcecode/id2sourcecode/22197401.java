    public int map(int addr, int len, boolean read, boolean write, boolean exec) throws MemoryMapException {
        return mem.map(addr, len, read, write, exec);
    }
