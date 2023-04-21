    public void run() {
        backend = searchFactoryImplementor.makeBatchBackend(monitor, writerThreads);
        try {
            beforeBatch();
            doBatchWork();
            backend.stopAndFlush(60L * 60 * 24, TimeUnit.SECONDS);
            afterBatch();
        } catch (InterruptedException e) {
            log.error("Batch indexing was interrupted");
            Thread.currentThread().interrupt();
        } finally {
            backend.close();
            monitor.indexingCompleted();
        }
    }
