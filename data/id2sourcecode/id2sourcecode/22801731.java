    private FileLock createFileLock(File f) {
        try {
            File file = f;
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            return channel.tryLock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
