    public synchronized long skip(long n) throws IOException {
        if (n <= 0) return 0;
        long avail = writepos - readpos;
        if (avail < n) {
            long req = n - avail;
            if (req > Integer.MAX_VALUE) req = Integer.MAX_VALUE;
            fill((int) req);
            avail = writepos - readpos;
            if (avail <= 0) return 0;
        }
        long skipped = (avail < n) ? avail : n;
        readpos += skipped;
        return skipped;
    }
