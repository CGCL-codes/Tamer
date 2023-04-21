    public SeisLock(String path, int retryCount, long maxsleep) throws SeisException {
        if (path == null || path.length() < 1) throw new IllegalArgumentException("Invalid path");
        _path = path + EXTN;
        isLocked = false;
        if (retryCount < 1) throw new IllegalArgumentException("Retry count parameter is invalid");
        retry = retryCount;
        maxSleep = maxsleep > 10000 ? 10000 : maxsleep;
        try {
            lockFile = new RandomAccessFile(_path, "rw");
            fc = lockFile.getChannel();
        } catch (FileNotFoundException e) {
            throw new SeisException(e.toString());
        }
    }
