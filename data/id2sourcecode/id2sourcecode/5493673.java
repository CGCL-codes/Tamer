    public void close() {
        if (writerThread != null && writerThread.isAlive()) writerThread.interrupt();
        if (archivable != null) {
            archivable.close();
            archivable = null;
        }
        unexport(true);
    }
