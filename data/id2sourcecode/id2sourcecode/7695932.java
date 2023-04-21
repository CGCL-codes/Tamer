    @Override
    public void start() throws IOException {
        logger.finer(getClass().getSimpleName() + " start");
        for (int track = 0; track < numTracks; ++track) {
            writerThreads[track].start();
        }
    }
