    @Override
    public int read() throws IOException {
        boolean wasInterrupted = false;
        try {
            synchronized (synchronizer) {
                if (isClosed) throw new IOException("This StreamGobbler is closed.");
                while (read_pos == write_pos) {
                    if (exception != null) throw exception;
                    if (isEOF) return -1;
                    try {
                        synchronizer.wait();
                    } catch (InterruptedException e) {
                        wasInterrupted = true;
                    }
                }
                return buffer[read_pos++] & 0xff;
            }
        } finally {
            if (wasInterrupted) Thread.currentThread().interrupt();
        }
    }
