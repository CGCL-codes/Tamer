    public void writeRunStatus(Memory mem, int address) {
        start(mem, address);
        super.write();
        write32(status);
        write32(currentPriority);
        write32(waitType);
        write32(waitId);
        write32(wakeupCount);
        write64(runClocks);
        write32(intrPreemptCount);
        write32(threadPreemptCount);
        write32(releaseCount);
    }
