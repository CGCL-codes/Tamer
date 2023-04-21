    public static int ioWaitSelect(int[] readFds, int[] writeFds, int[] exceptFds, double totalWaitTime, boolean fromNative) {
        if (fromNative) {
            if (!Processor.getCurrentProcessor().threadSwitchingEnabled() || Scheduler.getCurrentThread().getDisallowAllocationsByThisThread()) {
                return -1;
            }
        }
        long maxWaitNano = getMaxWaitNano(totalWaitTime);
        ThreadIOWaitData waitData = new ThreadIOWaitData(maxWaitNano);
        waitData.readFds = readFds;
        waitData.writeFds = writeFds;
        waitData.exceptFds = exceptFds;
        if (fromNative) {
            waitData.setNative();
        }
        if (noIoWait) {
            waitData.markAllAsReady();
        } else {
            GreenThread.ioWaitImpl(waitData);
        }
        return 0;
    }
