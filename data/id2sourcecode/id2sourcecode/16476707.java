    public synchronized int read() throws IOException {
        if (readpos >= writepos) {
            fill(1);
            if (readpos >= writepos) return -1;
        }
        return buf[readpos++] & 0xff;
    }
