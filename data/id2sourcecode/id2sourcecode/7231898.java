    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(blockSize);
        write32(numBlocks);
        write32(freeBlocks);
        write32(numWaitThreads);
    }
