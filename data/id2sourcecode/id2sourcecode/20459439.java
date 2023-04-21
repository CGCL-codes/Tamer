    public static boolean lock() {
        final String path = SqlTablet.getLocalPath();
        final String filename = "sqltablet.lock";
        File file = new File(path + filename);
        file.deleteOnExit();
        try {
            channel = new RandomAccessFile(file, "rw").getChannel();
        } catch (FileNotFoundException e) {
            RemoteLogger.getInstance().logError(e);
            return false;
        }
        try {
            lock = channel.lock();
        } catch (IOException e) {
            RemoteLogger.getInstance().logError(e);
            return false;
        }
        return true;
    }
