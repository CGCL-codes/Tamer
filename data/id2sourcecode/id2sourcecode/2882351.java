    public boolean lockServerInstance() {
        try {
            if (lckFile == null) {
                lckFile = new RandomAccessFile(new File(SERVER_LOCK), "rw");
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
        ResourcePool.logError("A " + SERVER_LOCK + " file exists! A server maybe already running.");
        closeServerInstance(-1);
        return false;
    }
