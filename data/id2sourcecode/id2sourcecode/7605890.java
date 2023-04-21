    private NoMoreDataActionFileSize testForNoMoreDataAction(final EnumNoMoreDataAction noMoreDataAction, final boolean useCache) throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, InterruptedException, FilePersistenceTooBigForSerializationException {
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName(path.getPath());
        builder.setProxyMode(true);
        builder.setRemoveFiles(true);
        builder.setGarbageManagement(false);
        builder.setCrashSafeMode(false);
        builder.setNoMoreDataAction(noMoreDataAction);
        if (useCache) {
            builder.setFileCache(1024, 1024);
        }
        filePersistence = builder.build();
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (TestFileManagementOptions.this) {
                    TestFileManagementOptions.this.notifyAll();
                    final File file = new File(path, "store.data");
                    RandomAccessFile randomAccessFile;
                    try {
                        randomAccessFile = new RandomAccessFile(file, "rws");
                        FileLock lock = null;
                        try {
                            do {
                                try {
                                    lock = randomAccessFile.getChannel().tryLock();
                                } catch (final Exception e) {
                                    lock = null;
                                }
                                if (lock == null && !stop) {
                                    TestFileManagementOptions.this.wait(0);
                                }
                            } while (!stop && lock == null);
                            if (!stop) {
                                TestFileManagementOptions.this.wait();
                            }
                        } catch (final Exception e) {
                        }
                        try {
                            lock.release();
                        } catch (final Exception e) {
                        }
                        try {
                            randomAccessFile.close();
                        } catch (final Exception e) {
                        }
                    } catch (final Exception e1) {
                    }
                    TestFileManagementOptions.this.notifyAll();
                }
            }
        });
        synchronized (this) {
            stop = false;
            thread.start();
            notifyAll();
            wait();
        }
        final NoMoreDataActionFileSize noMoreDataActionFileSize = new NoMoreDataActionFileSize();
        try {
            final IDataAccessSession session = filePersistence.createDataAccessSession();
            session.open();
            session.setObject(KEY, new Object());
            session.close(EnumFilePersistenceCloseAction.SAVE);
            final File storageFile = new File(filePersistence.getStorageFileName());
            noMoreDataActionFileSize.before = storageFile.length();
            session.open();
            session.removeObject(KEY);
            session.close(EnumFilePersistenceCloseAction.SAVE);
            noMoreDataActionFileSize.after = storageFile.length();
            session.open();
            final Object read = session.getObject(KEY);
            assertNull("must be erased", read);
            session.setObject(KEY, new Object());
            session.close(EnumFilePersistenceCloseAction.SAVE);
            filePersistence.close();
        } finally {
            synchronized (this) {
                stop = true;
                notifyAll();
                wait();
            }
        }
        return noMoreDataActionFileSize;
    }
