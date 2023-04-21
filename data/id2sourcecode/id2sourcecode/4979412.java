    private void writerCleanup() throws IOException {
        synchronized (stateLock) {
            writerThread = 0;
            if (state == ST_KILLPENDING) kill();
        }
    }
