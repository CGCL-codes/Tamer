    public static boolean isFileLocked(final String fileName) throws FileNotFoundException {
        boolean result = true;
        final RandomAccessFile raf = new RandomAccessFile(new File(fileName), "rw");
        FileChannel channel = null;
        try {
            channel = raf.getChannel();
            final FileLock lock = channel.tryLock();
            if (lock != null) {
                lock.release();
                result = false;
            }
        } catch (IOException e) {
            result = true;
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, e.toString(), e);
                }
            }
            try {
                raf.close();
            } catch (IOException e) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, e.toString(), e);
            }
        }
        return result;
    }
