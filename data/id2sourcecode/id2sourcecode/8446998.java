    private boolean canObtainLock(File lockFile) {
        try {
            RandomAccessFile in = new RandomAccessFile(lockFile, "rw");
            FileChannel channel = in.getChannel();
            FileLock lock = channel.tryLock();
            if (lock == null) {
                return false;
            }
            lock.release();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
