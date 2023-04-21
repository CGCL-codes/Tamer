    public synchronized void DeleteKey(long key) throws IOException {
        if (key != (key & KEYMASK)) {
            throw new IOException("Invalid Key! " + key);
        }
        Find(key);
        long pos = RAF.getFilePointer();
        if (pos <= (RAF.length() - RECORDSIZE)) {
            long kv = RAF.readLong();
            RAF.readLong();
            if (kv == key) {
                File tf = extractToEnd();
                RAF.seek(pos);
                attachToEnd(tf);
                RAF.getChannel().truncate(RAF.length() - RECORDSIZE);
                DeleteFile(tf);
                if (RAF.length() == 0) {
                    FirstKey = Long.MIN_VALUE;
                    LastKey = Long.MIN_VALUE;
                } else {
                    if (pos == 0) {
                        RAF.seek(0);
                        FirstKey = RAF.readLong();
                    }
                    if (pos == RAF.length()) {
                        RAF.seek(pos - RECORDSIZE);
                        LastKey = RAF.readLong();
                    }
                }
                DeleteKey(key);
            }
        }
    }
