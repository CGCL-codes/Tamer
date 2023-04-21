    private void onMbxModified(SceKernelMbxInfo info) {
        ThreadManForUser threadMan = Modules.ThreadManForUserModule;
        boolean reschedule = false;
        if ((info.attr & PSP_MBX_ATTR_PRIORITY) == PSP_MBX_ATTR_FIFO) {
            for (Iterator<SceKernelThreadInfo> it = threadMan.iterator(); it.hasNext(); ) {
                SceKernelThreadInfo thread = it.next();
                if (thread.isWaitingForType(PSP_WAIT_MBX) && thread.wait.Mbx_id == info.uid && info.hasMessage()) {
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("onMbxModified waking thread %s", thread.toString()));
                    }
                    Memory mem = Memory.getInstance();
                    int msgAddr = info.removeMsg(mem);
                    mem.write32(thread.wait.Mbx_resultAddr, msgAddr);
                    info.numWaitThreads--;
                    thread.cpuContext.gpr[2] = 0;
                    threadMan.hleChangeThreadState(thread, PSP_THREAD_READY);
                    reschedule = true;
                }
            }
        } else if ((info.attr & PSP_MBX_ATTR_PRIORITY) == PSP_MBX_ATTR_PRIORITY) {
            for (Iterator<SceKernelThreadInfo> it = threadMan.iteratorByPriority(); it.hasNext(); ) {
                SceKernelThreadInfo thread = it.next();
                if (thread.isWaitingForType(PSP_WAIT_MBX) && thread.wait.Mbx_id == info.uid && info.hasMessage()) {
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("onMbxModified waking thread %s", thread.toString()));
                    }
                    Memory mem = Memory.getInstance();
                    int msgAddr = info.removeMsg(mem);
                    mem.write32(thread.wait.Mbx_resultAddr, msgAddr);
                    info.numWaitThreads--;
                    thread.cpuContext.gpr[2] = 0;
                    threadMan.hleChangeThreadState(thread, PSP_THREAD_READY);
                    reschedule = true;
                }
            }
        }
        if (reschedule) {
            threadMan.hleRescheduleCurrentThread();
        }
    }
