    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(numWaitThreads);
        write32(numMessages);
        write32(firstMessageAddr);
    }
