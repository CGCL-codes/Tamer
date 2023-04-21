    public static boolean lockMonitorInstance() {
        try {
            if (lckFile == null) {
                lckFile = new RandomAccessFile(new File(MONITOR_LOCK), "rw");
            }
            channel = lckFile.getChannel();
            exLck = channel.tryLock(1, 1, false);
            if (exLck != null) {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        ResourcePool.logError("A " + MONITOR_LOCK + " file exists! A monitor maybe already running.");
        if (exLck != null) {
            try {
                exLck.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (lckFile != null) {
            try {
                lckFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lckFile = null;
        }
        System.exit(EXIT_CODES.INVALIDSTATE.ordinal());
        return false;
    }
