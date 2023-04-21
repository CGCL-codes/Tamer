    @Override
    public void connect() throws IOException {
        logger.finer(getClass().getSimpleName() + " connect");
        this.pushSourceStreams = new InputStreamPushSourceStream[numTracks];
        this.writerThreads = new WriterThread[numTracks];
        for (int track = 0; track < numTracks; ++track) {
            final StreamPipe p = new StreamPipe();
            pushSourceStreams[track] = new InputStreamPushSourceStream(outputContentDescriptor, p.getInputStream());
            writerThreads[track] = new WriterThread(track, inputStreams[track], p.getOutputStream(), inputFormats[track]);
            writerThreads[track].setName("WriterThread for track " + track);
            writerThreads[track].setDaemon(true);
        }
    }
