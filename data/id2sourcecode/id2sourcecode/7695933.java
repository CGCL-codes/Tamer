    @Override
    public void stop() throws IOException {
        logger.finer(getClass().getSimpleName() + " stop");
        for (int track = 0; track < numTracks; ++track) {
            writerThreads[track].close();
        }
        try {
            for (int track = 0; track < numTracks; ++track) {
                writerThreads[track].waitUntilClosed();
            }
        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        }
    }
