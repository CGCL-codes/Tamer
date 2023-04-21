    @Deprecated
    public void lock(Path p, boolean shared) throws IOException {
        File f = pathToFile(p);
        f.createNewFile();
        if (shared) {
            FileInputStream lockData = new FileInputStream(f);
            FileLock lockObj = lockData.getChannel().lock(0L, Long.MAX_VALUE, shared);
            synchronized (this) {
                sharedLockDataSet.put(f, lockData);
                lockObjSet.put(f, lockObj);
            }
        } else {
            FileOutputStream lockData = new FileOutputStream(f);
            FileLock lockObj = lockData.getChannel().lock(0L, Long.MAX_VALUE, shared);
            synchronized (this) {
                nonsharedLockDataSet.put(f, lockData);
                lockObjSet.put(f, lockObj);
            }
        }
    }
