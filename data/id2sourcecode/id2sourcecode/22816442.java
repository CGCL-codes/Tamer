    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) throw new NullPointerException();
        if ((off < 0) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0) || (off > b.length)) throw new IndexOutOfBoundsException();
        if (len == 0) return 0;
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
            int avail = write_pos - read_pos;
            avail = (avail > len) ? len : avail;
            System.arraycopy(buffer, read_pos, b, off, avail);
            read_pos += avail;
            return avail;
        }
    }
