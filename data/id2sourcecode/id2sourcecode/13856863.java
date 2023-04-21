    private long[] getDisksIO() {
        if (fileSystems.length == 0) {
            return SIGAR_ERROR_ARRAY;
        }
        long[] ret = { 0L, 0L };
        try {
            for (int i = 0; i < fileSystems.length; i++) {
                FileSystemUsage metrics = sigarProxy.getFileSystemUsage(fileSystems[i].getDevName());
                long reads = metrics.getDiskReads();
                long writes = metrics.getDiskWrites();
                if (reads == -1L || writes == -1L) {
                    return SIGAR_ERROR_ARRAY;
                } else {
                    ret[0] = ret[0] + reads;
                    ret[1] = ret[1] + writes;
                }
            }
        } catch (SigarException ex) {
            ServerAgent.logMessage(ex.getMessage());
            ret = AGENT_ERROR_ARRAY;
        }
        return ret;
    }
