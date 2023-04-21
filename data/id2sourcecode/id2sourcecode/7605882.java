    public void testFileOperationRetry() throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, FileIOException, IOException, FilePersistenceTooBigForSerializationException {
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName(path.getPath());
        builder.setProxyMode(true);
        builder.setRemoveFiles(false);
        builder.setGarbageManagement(false);
        builder.setCrashSafeMode(false);
        builder.setMaxFileOperationRetry(4);
        builder.setFileOperationRetryMsDelay(5000);
        final File file = new File(path, "store.data");
        HELPER_FILE_UTIL.touchFile(file, 1, 0);
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
        final FileLock lock = randomAccessFile.getChannel().tryLock();
        assertNotNull("must lock", lock);
        final int unLockDelay = 10000;
        final Timer timer = new Timer(true);
        try {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    try {
                        lock.release();
                    } catch (final IOException e) {
                    }
                    try {
                        randomAccessFile.close();
                    } catch (final IOException e) {
                    }
                }
            }, unLockDelay);
            filePersistence = builder.build();
            final IDataAccessSession session = filePersistence.createDataAccessSession();
            session.open();
            session.setObject(KEY, new Object());
            session.close(EnumFilePersistenceCloseAction.SAVE);
            filePersistence.close();
        } finally {
            try {
                timer.cancel();
            } catch (final Exception e) {
            }
            try {
                Thread.sleep(unLockDelay);
            } catch (final InterruptedException e) {
            }
            try {
                lock.release();
            } catch (final IOException e) {
            }
            try {
                randomAccessFile.close();
            } catch (final IOException e) {
            }
        }
    }
