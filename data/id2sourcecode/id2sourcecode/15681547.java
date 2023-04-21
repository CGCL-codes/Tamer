    private synchronized int Find(long key) throws IOException {
        if (key != (key & KEYMASK)) {
            throw new IOException("Invalid Key! " + key);
        }
        if (key <= FirstKey) {
            RAF.seek(0);
            return -1;
        } else if (key > LastKey) {
            RAF.seek(RAF.length());
            return 1;
        } else {
            long numrecords = RAF.length() / RECORDSIZE;
            long lastlow = 0;
            long lasthi = numrecords;
            long pos = numrecords / 2;
            while (lasthi > (lastlow + 1)) {
                RAF.seek(pos * RECORDSIZE);
                long tk = RAF.readLong();
                if (tk < key) {
                    lastlow = pos;
                    pos = (lasthi + pos) / 2;
                } else {
                    lasthi = pos;
                    pos = (lastlow + pos) / 2;
                }
            }
            RAF.seek(pos * RECORDSIZE);
            long tk = RAF.readLong();
            if (tk >= key) {
                RAF.seek(pos * RECORDSIZE);
            } else {
                RAF.readLong();
            }
            return 0;
        }
    }
