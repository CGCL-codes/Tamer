    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int avail = writepos - readpos;
        if (avail < len) {
            fill(len - avail);
            avail = writepos - readpos;
            if (avail <= 0) return -1;
        }
        int cnt = (avail < len) ? avail : len;
        System.arraycopy(buf, readpos, b, off, cnt);
        readpos += cnt;
        return cnt;
    }
