    private static VM_ThreadIOWaitData createIOWaitDataForSelect(int[] readFds, int[] writeFds, int[] exceptFds, double maxWaitTime) {
        VM_ThreadIOWaitData data = new VM_ThreadIOWaitData(maxWaitTime);
        data.readFds = readFds;
        data.writeFds = writeFds;
        data.exceptFds = exceptFds;
        return data;
    }
