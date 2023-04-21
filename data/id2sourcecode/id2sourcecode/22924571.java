    public void lock(final File file) throws FilePersistenceException {
        synchronized (this) {
            final Exception lockerTrace = new Exception("locker");
            final Exception previous = lockedDirMap.put(file, lockerTrace);
            if (previous != null) {
                throw new FilePersistenceException("File " + file + " already in use", previous);
            }
            file.mkdirs();
            final File lockFile = new File(file, LOCK);
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(lockFile);
                final FileChannel channel = fileOutputStream.getChannel();
                final FileLock fileLock = channel.tryLock();
                if (fileLock == null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException exception) {
                        LOGGER.error(exception);
                    }
                    throw new FilePersistenceException("File " + file + " already in use");
                }
                fileLockMap.put(lockFile, fileLock);
                lockFileOutputStreamMap.put(lockFile, fileOutputStream);
            } catch (IOException exception) {
                throw new FilePersistenceException(exception);
            }
        }
    }
