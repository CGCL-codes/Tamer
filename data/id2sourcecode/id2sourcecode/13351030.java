    public int sceKernelCancelEventFlag(int uid, int newPattern, int numWaitThreadAddr) {
        if (log.isDebugEnabled()) {
            log.debug("sceKernelCancelEventFlag uid=0x" + Integer.toHexString(uid) + " newPattern=0x" + Integer.toHexString(newPattern) + " numWaitThreadAddr=0x" + Integer.toHexString(numWaitThreadAddr));
        }
        SceUidManager.checkUidPurpose(uid, "ThreadMan-eventflag", true);
        SceKernelEventFlagInfo event = eventMap.get(uid);
        if (event == null) {
            log.warn("sceKernelCancelEventFlag unknown uid=0x" + Integer.toHexString(uid));
            return ERROR_KERNEL_NOT_FOUND_EVENT_FLAG;
        }
        Memory mem = Memory.getInstance();
        if (Memory.isAddressGood(numWaitThreadAddr)) {
            mem.write32(numWaitThreadAddr, event.numWaitThreads);
        }
        event.currentPattern = newPattern;
        event.numWaitThreads = 0;
        onEventFlagCancelled(uid);
        return 0;
    }
