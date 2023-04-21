    public static void ioWaitSelect(int[] readFds, int[] writeFds, int[] exceptFds, double totalWaitTime, boolean fromNative) {
        long maxWaitCycle = getMaxWaitCycle(totalWaitTime);
        VM_ThreadIOWaitData waitData = new VM_ThreadIOWaitData(maxWaitCycle);
        waitData.readFds = readFds;
        waitData.writeFds = writeFds;
        waitData.exceptFds = exceptFds;
        if (fromNative) waitData.waitFlags |= VM_ThreadEventConstants.WAIT_NATIVE;
        if (noIoWait) waitData.markAllAsReady(); else VM_Thread.ioWaitImpl(waitData);
    }
