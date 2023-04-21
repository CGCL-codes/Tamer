    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(poolSize);
        write32(freeSize);
        write32(numWaitThreads);
    }
