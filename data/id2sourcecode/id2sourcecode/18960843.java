    public QueuedArchive() {
        ArchiveWriter writeThread = new ArchiveWriter(this);
        writeThread.start();
    }
