    public synchronized List<IndexKeyValue> deleteBeforeKey(long key, int max) throws IOException {
        if (key != (key & KEYMASK)) {
            throw new IOException("Invalid Key! " + key);
        }
        LinkedList<IndexKeyValue> rl = new LinkedList<IndexKeyValue>();
        if (RAF.length() >= RECORDSIZE) {
            RAF.seek(0);
            long kv = Long.MIN_VALUE;
            long vv = Long.MIN_VALUE;
            while (RAF.getFilePointer() < RAF.length() && kv <= key && max > 0) {
                kv = RAF.readLong();
                vv = RAF.readLong();
                if (kv <= key) {
                    rl.add(new IndexKeyValue(kv, vv));
                }
                max--;
            }
            if (kv > key) {
                RAF.seek(RAF.getFilePointer() - RECORDSIZE);
            }
            if (rl.size() > 0) {
                long newsize = RAF.length() - RAF.getFilePointer();
                File tf = extractToEnd();
                RAF.seek(0);
                attachToEnd(tf);
                DeleteFile(tf);
                RAF.getChannel().truncate(newsize);
            }
        }
        return rl;
    }
