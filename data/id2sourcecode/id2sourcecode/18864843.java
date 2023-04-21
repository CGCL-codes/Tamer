        private void openFiles() throws IOException {
            LogManager manager = LogManager.getLogManager();
            manager.checkAccess();
            InitializationErrorManager em = new InitializationErrorManager();
            setErrorManager(em);
            int unique = -1;
            while (true) {
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
            files = new File[] { generate(pattern, 0, unique) };
            open(files[0], true);
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
        }
