    protected void addRecords(String tablename, int from, int to) {
        Transaction t = db.readwriteTran();
        for (int i = from; i <= to; ++i) t.addRecord(tablename, record(i));
        t.ck_complete();
    }
