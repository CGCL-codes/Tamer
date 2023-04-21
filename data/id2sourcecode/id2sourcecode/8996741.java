    public void sceKernelCancelVpl(int uid, int numWaitThreadAddr) {
        CpuState cpu = Emulator.getProcessor().cpu;
        if (log.isDebugEnabled()) {
            log.debug("sceKernelCancelVpl(uid=0x" + Integer.toHexString(uid) + ",numWaitThreadAddr=0x" + Integer.toHexString(numWaitThreadAddr) + ")");
        }
        SceKernelVplInfo info = vplMap.get(uid);
        if (info == null) {
            log.warn("sceKernelCancelVpl unknown uid=0x" + Integer.toHexString(uid));
            cpu.gpr[2] = ERROR_KERNEL_NOT_FOUND_VPOOL;
        } else {
            Memory mem = Memory.getInstance();
            if (Memory.isAddressGood(numWaitThreadAddr)) {
                mem.write32(numWaitThreadAddr, info.numWaitThreads);
            }
            cpu.gpr[2] = 0;
            onVplCancelled(uid);
        }
    }
