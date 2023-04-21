    LockedFile(File file, boolean readOnly) throws IOException {
        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
        }
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, readOnly ? "r" : "rw");
            FileLock lock = raf.getChannel().tryLock(0, Long.MAX_VALUE, readOnly);
            if (lock == null) {
                throw new DatabaseException("Database is open and locked by another process");
            }
        } catch (FileNotFoundException e) {
            if (readOnly) {
                raf = null;
            } else {
                throw e;
            }
        } catch (OverlappingFileLockException e) {
            throw new DatabaseException("Database is already open by current process");
        }
        mRaf = raf;
    }
