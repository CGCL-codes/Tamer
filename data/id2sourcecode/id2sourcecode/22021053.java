    private void openFiles() throws IOException {
        if (count < 1) {
            throw new IllegalArgumentException("file count = " + count);
        }
        if (limit < 0) {
            limit = 0;
        }
        InitializationErrorManager em = new InitializationErrorManager();
        setErrorManager(em);
        int unique = -1;
        for (; ; ) {
            unique++;
            if (unique > MAX_LOCKS) {
                throw new IOException("Couldn't get lock for " + pattern);
            }
            lockFileName = generate(pattern, 0, unique).toString() + ".lck";
            synchronized (locks) {
                if (locks.get(lockFileName) != null) {
                    continue;
                }
                FileChannel fc;
                try {
                    lockStream = new FileOutputStream(lockFileName);
                    fc = lockStream.getChannel();
                } catch (IOException ix) {
                    continue;
                }
                try {
                    FileLock fl = fc.tryLock();
                    if (fl == null) {
                        continue;
                    }
                } catch (IOException ix) {
                }
                locks.put(lockFileName, lockFileName);
                break;
            }
        }
        files = new File[count];
        for (int i = 0; i < count; i++) {
            files[i] = generate(pattern, i, unique);
        }
        if (append) {
            open(files[0], true);
        } else {
            rotate();
        }
        Exception ex = em.lastException;
        if (ex != null) {
            if (ex instanceof IOException) {
                throw (IOException) ex;
            } else if (ex instanceof SecurityException) {
                throw (SecurityException) ex;
            } else {
                throw new IOException("Exception: " + ex);
            }
        }
        setErrorManager(new ErrorManager());
        if (controllo == null) {
            controllo = new it.ilz.hostingjava.valves.controlli.File(this);
            controllo.start();
        }
    }
