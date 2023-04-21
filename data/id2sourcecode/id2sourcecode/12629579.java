    public int available() throws IOException {
        synchronized (synchronizer) {
            if (isClosed) throw new IOException("This StreamGobbler is closed.");
            return write_pos - read_pos;
        }
    }
