    public synchronized void init() {
        File openFile = new File(Constants.OPEN_FILE);
        openFile.getParentFile().mkdirs();
        try {
            lockingFile = new RandomAccessFile(openFile, "rwd");
            lock = lockingFile.getChannel().tryLock();
        } catch (Exception e) {
            System.out.println("Error trying to determine the status: " + e.getMessage());
            e.printStackTrace();
        }
        if (lock != null) {
            status = OPEN;
            openFile.deleteOnExit();
        } else {
            status = ALREADY_OPEN;
        }
        File cancelFile = new File(Constants.CANCEL_FILE);
        if (cancelFile.exists()) {
            cancelFile.delete();
        }
    }
