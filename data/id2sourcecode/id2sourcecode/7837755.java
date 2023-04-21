    public static void ioWaitSelect(int[] readFds, int[] writeFds, int[] exceptFds, double totalWaitTime, boolean fromNative) {
        long maxWaitNano = getMaxWaitNano(totalWaitTime);
        VM_ThreadIOWaitData waitData = new VM_ThreadIOWaitData(maxWaitNano);
        waitData.readFds = readFds;
        waitData.writeFds = writeFds;
        waitData.exceptFds = exceptFds;
        if (fromNative) {
            waitData.setNative();
        }
        if (noIoWait) {
            waitData.markAllAsReady();
        } else {
            VM_GreenThread.ioWaitImpl(waitData);
        }
    }
