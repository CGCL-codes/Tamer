    public void waitUntilFinished() throws InterruptedException {
        try {
            for (int track = 0; track < numTracks; ++track) {
                writerThreads[track].waitUntilClosed();
            }
        } catch (InterruptedException e) {
            throw e;
        }
    }
