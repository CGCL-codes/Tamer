    public void sceKernelCancelMsgPipe(int uid, int send_addr, int recv_addr) {
        CpuState cpu = Emulator.getProcessor().cpu;
        Memory mem = Emulator.getMemory();
        if (log.isDebugEnabled()) {
            log.debug("sceKernelCancelMsgPipe(uid=0x" + Integer.toHexString(uid) + ", send=0x" + Integer.toHexString(send_addr) + ", recv=0x" + Integer.toHexString(recv_addr) + ")");
        }
        SceKernelMppInfo info = msgMap.get(uid);
        if (info == null) {
            log.warn("sceKernelCancelMsgPipe unknown uid=0x" + Integer.toHexString(uid));
            cpu.gpr[2] = ERROR_KERNEL_NOT_FOUND_MESSAGE_PIPE;
        } else {
            if (Memory.isAddressGood(send_addr)) {
                mem.write32(send_addr, info.numSendWaitThreads);
            }
            if (Memory.isAddressGood(recv_addr)) {
                mem.write32(recv_addr, info.numReceiveWaitThreads);
            }
            info.numSendWaitThreads = 0;
            info.numReceiveWaitThreads = 0;
            cpu.gpr[2] = 0;
            onMsgPipeCancelled(uid);
        }
    }
