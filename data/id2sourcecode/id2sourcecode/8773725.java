    synchronized void lockFile(String path, Object file) {
        String canonicalPath;
        try {
            canonicalPath = new File(path).getCanonicalPath();
        } catch (IOException e) {
            throw new Db4oIOException(e);
        }
        if (fileLocks == null) {
            fileLocks = new Hashtable();
        }
        if (fileLocks.containsKey(canonicalPath)) {
            throw new DatabaseFileLockedException(canonicalPath);
        }
        Object lock = null;
        Object channel = Reflection4.invoke(file, "getChannel");
        try {
            lock = Reflection4.invoke(channel, "tryLock");
        } catch (ReflectException rex) {
            throw new DatabaseFileLockedException(canonicalPath, rex);
        }
        if (lock == null) {
            throw new DatabaseFileLockedException(canonicalPath);
        }
        fileLocks.put(canonicalPath, lock);
    }
