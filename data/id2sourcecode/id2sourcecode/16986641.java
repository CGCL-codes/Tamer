    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(status);
        write32(entry_addr);
        write32(stackAddr);
        write32(stackSize);
        write32(gpReg_addr);
        write32(initPriority);
        write32(currentPriority);
        write32(getPSPWaitType());
        write32(waitId);
        write32(wakeupCount);
        write32(exitStatus);
        write64(runClocks);
        write32(intrPreemptCount);
        write32(threadPreemptCount);
        write32(releaseCount);
    }
