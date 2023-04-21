    public static void lockFileAndWait(VFile f, long milliSecs) {
        FileLock lock = null;
        try {
            FileChannel ch = new FileOutputStream(f).getChannel();
            lock = ch.tryLock();
            while (lock == null) {
                Thread.sleep(100);
                lock = ch.tryLock();
            }
            Thread.sleep(milliSecs);
            lock.release();
        } catch (Exception e) {
            throw new VRuntimeException(e);
        } finally {
            if (lock != null) {
                try {
                    lock.release();
                } catch (Exception ignored) {
                }
            }
        }
    }
