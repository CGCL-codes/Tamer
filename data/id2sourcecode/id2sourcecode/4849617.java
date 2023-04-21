    private static int load_data(InputStream fin, String tablename) throws IOException {
        int nrecs = 0;
        byte[] buf = new byte[4];
        ByteBuffer bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
        byte[] recbuf = new byte[4096];
        Transaction tran = theDB.readwriteTran();
        try {
            for (; ; ++nrecs) {
                verify(fin.read(buf) == buf.length);
                int n = bb.getInt(0);
                if (n == 0) break;
                if (n > recbuf.length) recbuf = new byte[Math.max(n, 2 * recbuf.length)];
                load_data_record(fin, tablename, tran, recbuf, n);
                if (nrecs % 100 == 99) {
                    verify(tran.complete() == null);
                    tran = theDB.readwriteTran();
                }
            }
        } finally {
            tran.ck_complete();
        }
        return nrecs;
    }
