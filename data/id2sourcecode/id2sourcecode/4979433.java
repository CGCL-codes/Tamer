    public void kill() throws IOException {
        synchronized (stateLock) {
            if (state == ST_KILLED) return;
            if (state == ST_UNINITIALIZED) {
                state = ST_KILLED;
                return;
            }
            assert !isOpen() && !isRegistered();
            if (readerThread == 0 && writerThread == 0) {
                nd.close(fd);
                state = ST_KILLED;
            } else {
                state = ST_KILLPENDING;
            }
        }
    }
