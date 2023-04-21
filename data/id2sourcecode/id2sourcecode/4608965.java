    public void restore(final String snapshot, String base, String filenamePrefix, final ResultConsumer<String> consumer) throws AttributeStorageException, IOException, ObjectStorageException, NoSuchAlgorithmException, WorkQueueAbortedException, ParseException {
        if (!base.endsWith(File.separator)) {
            base += File.separator;
        }
        final String basePath = base;
        if (StringUtils.isNotEmpty(filenamePrefix)) {
            logInfo(LOG, "restoring files matching prefix %s in snapshot %s to %s", filenamePrefix, snapshot, basePath);
        } else {
            logInfo(LOG, "restoring all files in snapshot %s to %s", snapshot, basePath);
        }
        ExceptionHandler exceptionHandler = new ExceptionHandler() {

            public boolean handleException(Exception ex) {
                logError(LOG, ex, consumer, "error while restoring snapshot %s", snapshot);
                return true;
            }
        };
        final WorkQueue workQueue1 = new WorkQueue(exceptionHandler);
        final WorkQueue workQueue2 = new WorkQueue(exceptionHandler);
        try {
            shadowFileService.findStubs(snapshot, filenamePrefix, new ResultConsumer<ShadowFile>() {

                public void newResult(final ShadowFile result) throws Exception {
                    File onDisk = null;
                    final Semaphore sem = new Semaphore(0);
                    for (final String filename : result.getFilenames()) {
                        final String fullFilename = basePath + filename;
                        final File file = new File(fullFilename);
                        if (file.exists() && file.length() == result.getSize() && result.getKey().equals(shadowFileService.createShadowKey(new FileInputStream(file)))) {
                            if (onDisk == null) {
                                onDisk = file;
                            }
                            sem.release(Integer.MAX_VALUE);
                            logDebug(LOG, "file %s of size %d already exists", fullFilename, result.getSize());
                        } else if (onDisk != null) {
                            final File srcFile = onDisk;
                            workQueue1.enqueue(new WorkItem() {

                                public void run() throws Exception {
                                    sem.acquire();
                                    synchronized ("mkdir") {
                                        FileUtils.forceMkdir(file.getParentFile());
                                    }
                                    FileUtils.deleteQuietly(file);
                                    FileUtils.copyFile(srcFile, file);
                                    logInfo(LOG, "restored file %s with copy of %s", fullFilename, srcFile.getName());
                                    if (consumer != null) {
                                        consumer.newResult(fullFilename);
                                    }
                                }
                            });
                        } else {
                            synchronized ("mkdir") {
                                FileUtils.forceMkdir(file.getParentFile());
                            }
                            FileUtils.deleteQuietly(file);
                            onDisk = file;
                            if (result.getSize() == 0) {
                                FileUtils.touch(file);
                                sem.release(Integer.MAX_VALUE);
                                logInfo(LOG, "restored file %s", fullFilename);
                                if (consumer != null) {
                                    consumer.newResult(fullFilename);
                                }
                            } else {
                                final QuillenOutputFile out = new QuillenOutputFile(file, result.getSize(), new Runnable() {

                                    public void run() {
                                        sem.release(Integer.MAX_VALUE);
                                        try {
                                            if (result.getKey().equals(shadowFileService.createShadowKey(new FileInputStream(file)))) {
                                                logInfo(LOG, "restored file %s", fullFilename);
                                                if (consumer != null) {
                                                    consumer.newResult(fullFilename);
                                                }
                                            } else {
                                                logWarning(LOG, "file %s may have been corrupted", fullFilename);
                                            }
                                        } catch (Exception e) {
                                            logError(LOG, e, consumer, "error while trying to verify restored file %s", fullFilename);
                                        }
                                    }
                                });
                                workQueue1.enqueue(new WorkItem() {

                                    public void run() throws Exception {
                                        long currPos = 0;
                                        for (String ck : shadowFileService.get(result.getKey()).getChunkKeys()) {
                                            final String chunkKey = ck;
                                            final long pos = currPos;
                                            workQueue2.enqueue(new WorkItem() {

                                                public void run() throws IOException, NoSuchAlgorithmException, ObjectStorageException {
                                                    out.write(pos, chunkService.get(chunkKey).getData());
                                                }
                                            });
                                            currPos += Chunk.getSizeFromKey(chunkKey);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
            workQueue1.freezeAndBlockUntilEmpty();
            workQueue2.freezeAndBlockUntilEmpty();
        } finally {
            if (!workQueue1.isEmpty()) {
                workQueue1.abort();
            }
            if (!workQueue2.isEmpty()) {
                workQueue2.abort();
            }
        }
    }
