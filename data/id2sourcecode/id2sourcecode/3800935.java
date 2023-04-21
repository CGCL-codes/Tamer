    @Override
    public StageCondition run(StageCondition condition) throws InterruptedException {
        assert (condition.logicID == LogicID.LOAD) : "PROGRAMATICAL ERROR!";
        LSN actual = getState();
        LSN until = (condition.end == null) ? new LSN(actual.getViewId() + 1, 0L) : condition.end;
        MasterClient master = slaveView.getSynchronizationPartner(until);
        Logging.logMessage(Logging.LEVEL_INFO, this, "LOAD: Loading DB since %s from %s.", actual.toString(), master.getDefaultServerAddress().toString());
        DBFileMetaDataSet result = null;
        try {
            result = master.load(actual).get();
        } catch (Exception e) {
            Logging.logMessage(Logging.LEVEL_DEBUG, this, "LOAD: metadata could not be retrieved from Master (%s).", master.toString());
            Logging.logError(Logging.LEVEL_DEBUG, this, e);
            return condition;
        }
        if (result.size() == 0) {
            LSN lov;
            try {
                lov = babuDB.checkpoint();
                lastOnView.set(lov);
                Logging.logMessage(Logging.LEVEL_DEBUG, this, "LOAD: Logfile switched at LSN %s.", lov.toString());
                return finish(until);
            } catch (BabuDBException e) {
                Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: Taking a checkpoint failed.");
                Logging.logError(Logging.LEVEL_WARN, this, e);
                return condition;
            }
        }
        babuDB.stopBabuDB();
        try {
            fileIO.backupFiles();
        } catch (IOException e) {
            Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: File-backup failed.");
            Logging.logError(Logging.LEVEL_WARN, this, e);
            if (Thread.interrupted()) {
                try {
                    babuDB.startBabuDB();
                } catch (BabuDBException e1) {
                    Logging.logError(Logging.LEVEL_ERROR, this, e1);
                }
            }
            return condition;
        }
        final AtomicInteger openChunks = new AtomicInteger(result.size());
        LSN lsn = null;
        for (DBFileMetaData fileData : result) {
            final String fileName = fileData.file;
            String parentName = new File(fileName).getParentFile().getName();
            if (LSMDatabase.isSnapshotFilename(parentName)) {
                if (lsn == null) {
                    lsn = LSMDatabase.getSnapshotLSNbyFilename(parentName);
                } else if (!lsn.equals(LSMDatabase.getSnapshotLSNbyFilename(parentName))) {
                    Logging.logMessage(Logging.LEVEL_DEBUG, this, "LOAD: Indexfiles had ambiguous LSNs: %s", "LOAD will be retried.");
                    return condition;
                }
            }
            long fileSize = fileData.size;
            if (!(fileSize > 0L)) {
                Logging.logMessage(Logging.LEVEL_DEBUG, this, "LOAD: Empty file received -> retry.");
                return condition;
            }
            assert (maxChunkSize > 0L) : "Empty chunks are not allowed: " + fileName;
            synchronized (openChunks) {
                if (openChunks.get() != -1) {
                    openChunks.addAndGet((int) (fileSize / maxChunkSize));
                }
            }
            long begin = 0L;
            for (long end = maxChunkSize; end < (fileSize + maxChunkSize); end += maxChunkSize) {
                final long pos1 = begin;
                final long size = (end > fileSize) ? fileSize : end;
                begin = end;
                master.chunk(fileName, pos1, size).registerListener(new ClientResponseAvailableListener<ReusableBuffer>() {

                    @Override
                    public void responseAvailable(ReusableBuffer buffer) {
                        synchronized (openChunks) {
                            FileChannel fChannel = null;
                            try {
                                if (openChunks.get() < 0) throw new IOException();
                                if (buffer.remaining() == 0) {
                                    Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: CHUNK ERROR: Empty buffer received!");
                                    throw new IOException("CHUNK ERROR: Empty buffer received!");
                                }
                                File f = fileIO.getFile(fileName);
                                Logging.logMessage(Logging.LEVEL_DEBUG, this, "LOAD: SAVING %s to %s.", fileName, f.getPath());
                                assert (f.exists()) : "File '" + fileName + "' was not created properly.";
                                fChannel = new FileOutputStream(f).getChannel();
                                fChannel.write(buffer.getBuffer(), pos1);
                            } catch (IOException e) {
                                Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: Chunk request (%s,%d,%d) failed: %s", fileName, pos1, size, e.getMessage());
                                openChunks.set(-1);
                                openChunks.notify();
                                return;
                            } finally {
                                if (fChannel != null) {
                                    try {
                                        fChannel.close();
                                    } catch (IOException e) {
                                        Logging.logError(Logging.LEVEL_ERROR, this, e);
                                    }
                                }
                                if (buffer != null) BufferPool.free(buffer);
                            }
                            if (openChunks.get() != -1 && openChunks.decrementAndGet() == 0) openChunks.notify();
                        }
                    }

                    @Override
                    public void requestFailed(Exception e) {
                        if (e instanceof ErrorCodeException) {
                            ErrorCodeException err = (ErrorCodeException) e;
                            Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: Chunk request (%s,%d,%d) failed: (%d) %s", fileName, pos1, size, err.getCode());
                        } else {
                            Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: Chunk request (%s,%d,%d) failed: %s", fileName, pos1, size, e.getMessage());
                        }
                        synchronized (openChunks) {
                            openChunks.set(-1);
                            openChunks.notify();
                        }
                    }
                });
            }
        }
        synchronized (openChunks) {
            if (openChunks.get() > 0) openChunks.wait();
        }
        if (openChunks.get() == -1) {
            Logging.logMessage(Logging.LEVEL_DEBUG, this, "LOAD: At least one chunk could not have been inserted.");
            return condition;
        }
        try {
            babuDB.startBabuDB();
            fileIO.removeBackupFiles();
            assert (!actual.equals(babuDB.getState())) : "Loading the database had no effect!";
            return finish(until);
        } catch (BabuDBException e) {
            Logging.logMessage(Logging.LEVEL_WARN, this, "LOAD: Loading failed, because the " + "reloading the DBS failed due: %s", e.getMessage());
            return condition;
        }
    }
