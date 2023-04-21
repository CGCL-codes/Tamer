    public int read() throws IOException {
        synchronized (synchronizer) {
            if (isClosed) throw new IOException("This StreamGobbler is closed.");
            while (read_pos == write_pos) {
                if (exception != null) throw exception;
                if (isEOF) return -1;
                try {
                    synchronizer.wait();
                } catch (InterruptedException e) {
                }
            }
            int b = buffer[read_pos++] & 0xff;
            return b;
        }
    }
