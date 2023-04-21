    private void backupFile(final String base, final File file, final WorkQueue chunkWorkQueue, final MutexProvider mutexProvider, final ShadowFileBatch shadowFileBatch, final String tempFilePrefix) throws NoSuchAlgorithmException, IOException, WorkQueueAbortedException, ObjectStorageException, AttributeStorageException, InterruptedException {
        final String filename = StringUtils.removeStart(file.getAbsolutePath(), base);
        if (filename.getBytes("utf-8").length > MAX_FILENAME_BYTES) {
            throw new IllegalArgumentException(String.format("file name must be less than %d bytes (utf-8): %s", MAX_FILENAME_BYTES, filename));
        }
        File fileToProcess;
        final File tempFile = TempFileUtils.createTempFile(tempFilePrefix);
        try {
            FileUtils.copyFile(file, tempFile);
            fileToProcess = tempFile;
        } catch (IOException e) {
            logWarning(LOG, "failed to create temp copy at %s (%s), continuing with file %s.  This can be dangerous if the file is changing during backup!", tempFile.getAbsolutePath(), e.getMessage(), file.getAbsolutePath());
            FileUtils.deleteQuietly(tempFile);
            fileToProcess = file;
        }
        if (fileToProcess.exists()) {
            final String shadowKey = shadowFileService.createShadowKey(new FileInputStream(fileToProcess));
            synchronized (mutexProvider.getMutex("shadow-" + shadowKey)) {
                final ShadowFile shadowFile = shadowFileBatch.add(shadowKey, filename, fileToProcess.length());
                if (shadowFile != null) {
                    final StreamChunker chunker = new StreamChunker(new FileInputStream(fileToProcess));
                    final Semaphore sem = new Semaphore(0);
                    Chunk c;
                    while ((c = chunker.getNextChunk()) != null) {
                        final Chunk chunk = c;
                        shadowFile.addChunk(chunk);
                        chunkWorkQueue.enqueue(new WorkItem() {

                            public void run() throws ObjectStorageException, AttributeStorageException, IOException {
                                synchronized (mutexProvider.getMutex("chunk-" + chunk.getKey())) {
                                    chunkService.put(shadowKey, chunk);
                                }
                                sem.release();
                            }
                        });
                    }
                    sem.acquire(shadowFile.getChunkKeys().size());
                    shadowFileBatch.signalDone(shadowKey);
                }
            }
            FileUtils.deleteQuietly(tempFile);
        } else {
            logWarning(LOG, "file %s seems to have disappeared!", fileToProcess.toString());
        }
    }
