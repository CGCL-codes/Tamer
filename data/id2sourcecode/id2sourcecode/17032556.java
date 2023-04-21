    public void sceKernelCancelMutex(int uid, int newcount, int numWaitThreadAddr) {
        CpuState cpu = Emulator.getProcessor().cpu;
        Memory mem = Memory.getInstance();
        if (log.isDebugEnabled()) {
            log.debug("sceKernelCancelMutex uid=" + Integer.toHexString(uid) + ", newcount=" + newcount + ", numWaitThreadAddr=0x" + Integer.toHexString(numWaitThreadAddr));
        }
        SceKernelMutexInfo info = mutexMap.get(uid);
        if (info == null) {
            log.warn("sceKernelCancelMutex unknown UID " + Integer.toHexString(uid));
            cpu.gpr[2] = ERROR_KERNEL_MUTEX_NOT_FOUND;
        } else if (info.lockedCount == 0) {
            log.warn("sceKernelCancelMutex UID " + Integer.toHexString(uid) + " not locked");
            cpu.gpr[2] = -1;
        } else {
            if (Memory.isAddressGood(numWaitThreadAddr)) {
                mem.write32(numWaitThreadAddr, info.numWaitThreads);
            }
            if (newcount == -1) {
                info.lockedCount = info.initCount;
            } else {
                info.lockedCount = newcount;
            }
            cpu.gpr[2] = 0;
            onMutexCancelled(uid);
        }
    }
