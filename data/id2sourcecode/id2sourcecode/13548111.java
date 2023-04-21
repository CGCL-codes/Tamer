    static boolean canLock(String path) {
        FileChannel channel;
        RandomAccessFile raf = null;
        boolean result = true;
        try {
            raf = new RandomAccessFile(PATH, "rw");
            channel = raf.getChannel();
            FileLock lock = channel.tryLock();
            if (lock == null) {
                result = false;
            } else {
                lock.release();
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } catch (OverlappingFileLockException ofle) {
            result = false;
            ofle.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
