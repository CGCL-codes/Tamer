    public void stop() throws IOException {
        if (writerThread != null) {
            writerThread.close();
            try {
                writerThread.waitUntilClosed();
            } catch (InterruptedException e) {
                throw new InterruptedIOException();
            } finally {
                writerThread = null;
            }
        }
        if (source != null) source.stop();
    }
