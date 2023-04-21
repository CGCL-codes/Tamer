    public void sceKernelCancelReceiveMbx(int uid, int pnum_addr) {
        CpuState cpu = Emulator.getProcessor().cpu;
        Memory mem = Memory.getInstance();
        if (log.isDebugEnabled()) {
            log.debug("sceKernelCancelReceiveMbx(uid=0x" + Integer.toHexString(uid) + ")");
        }
        SceKernelMbxInfo info = mbxMap.get(uid);
        if (info == null) {
            log.warn("sceKernelCancelReceiveMbx unknown uid=0x" + Integer.toHexString(uid));
            cpu.gpr[2] = ERROR_KERNEL_NOT_FOUND_MESSAGE_BOX;
        } else {
            if (Memory.isAddressGood(pnum_addr)) {
                mem.write32(pnum_addr, info.numWaitThreads);
            }
            cpu.gpr[2] = 0;
            onMbxCancelled(uid);
        }
    }
