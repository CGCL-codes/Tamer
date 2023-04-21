    public synchronized void DeleteKey(long key, long val) throws IOException {
        if (key != (key & KEYMASK)) {
            throw new IOException("Invalid Key! " + key);
        }
        Find(key);
        long pos = RAF.getFilePointer();
        if (pos <= (RAF.length() - RECORDSIZE)) {
            long kv = RAF.readLong();
            long vv = RAF.readLong();
            if (kv == key) {
                boolean atend = false;
                File tf = File.createTempFile("tmp3", ".dat", new File(Root));
                RandomAccessFile traf = new RandomAccessFile(tf, "rw");
                while (kv == key && RAF.getFilePointer() < RAF.length()) {
                    if (vv != val) {
                        traf.writeLong(kv);
                        traf.writeLong(vv);
                    }
                    kv = RAF.readLong();
                    vv = RAF.readLong();
                }
                if (kv != key || vv != val) {
                    traf.writeLong(kv);
                    traf.writeLong(vv);
                }
                if (RAF.getFilePointer() < RAF.length()) {
                    traf.getChannel().transferFrom(RAF.getChannel(), traf.getFilePointer(), RAF.length() - RAF.getFilePointer());
                } else {
                    atend = true;
                }
                traf.close();
                RAF.seek(pos);
                attachToEnd(tf);
                RAF.getChannel().truncate(pos + tf.length());
                DeleteFile(tf);
                if (RAF.length() == 0) {
                    FirstKey = Long.MIN_VALUE;
                    LastKey = Long.MIN_VALUE;
                } else {
                    if (pos == 0) {
                        RAF.seek(0);
                        FirstKey = RAF.readLong();
                    }
                    if (atend) {
                        RAF.seek(RAF.length() - RECORDSIZE);
                        LastKey = RAF.readLong();
                    }
                }
            }
        }
    }
