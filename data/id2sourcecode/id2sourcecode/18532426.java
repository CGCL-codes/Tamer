    public static void ioWaitSelect(int[] readFds, int[] writeFds, int[] exceptFds, double totalWaitTime, boolean fromNative) {
        double maxWaitTime = getMaxWaitTime(totalWaitTime);
        VM_ThreadIOWaitData waitData = createIOWaitDataForSelect(readFds, writeFds, exceptFds, maxWaitTime);
        if (fromNative) waitData.waitFlags |= VM_ThreadEventConstants.WAIT_NATIVE;
        if (noIoWait) waitData.markAllAsReady(); else VM_Thread.ioWaitImpl(waitData);
    }
