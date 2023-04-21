    public void sceKernelCancelFpl(int uid, int numWaitThreadAddr) {
        CpuState cpu = Emulator.getProcessor().cpu;
        if (log.isDebugEnabled()) {
            log.debug("sceKernelCancelFpl(uid=0x" + Integer.toHexString(uid) + ",numWaitThreadAddr=0x" + Integer.toHexString(numWaitThreadAddr) + ")");
        }
        SceKernelFplInfo info = fplMap.get(uid);
        if (info == null) {
            log.warn("sceKernelCancelFpl unknown uid=0x" + Integer.toHexString(uid));
            cpu.gpr[2] = ERROR_KERNEL_NOT_FOUND_FPOOL;
        } else {
            Memory mem = Memory.getInstance();
            if (Memory.isAddressGood(numWaitThreadAddr)) {
                mem.write32(numWaitThreadAddr, info.numWaitThreads);
            }
            cpu.gpr[2] = 0;
            onFplCancelled(uid);
        }
    }
