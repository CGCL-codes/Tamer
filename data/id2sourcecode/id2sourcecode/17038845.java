    @Override
    protected void write() {
        super.write();
        write32(status);
        write64(idleClocks);
        write32(comesOutOfIdleCount);
        write32(threadSwitchCount);
        write32(vfpuSwitchCount);
    }
