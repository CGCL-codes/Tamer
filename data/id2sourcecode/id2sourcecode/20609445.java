    private static MyFileLock tryLockFile(File lockFile) throws IOException {
        if (!lockFile.isFile()) return null;
        RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
        FileLock fl = null;
        try {
            fl = raf.getChannel().tryLock();
        } catch (Exception e) {
        }
        if (fl != null) {
            return new MyFileLock(raf, fl, lockFile);
        }
        return null;
    }
