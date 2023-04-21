    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(initCount);
        write32(currentCount);
        write32(maxCount);
        write32(numWaitThreads);
    }
