    private void copyTable(String tablename) {
        Table table = rt.ck_getTable(tablename);
        List<String> fields = table.getFields();
        boolean squeeze = DbDump.needToSqueeze(fields);
        Index index = table.indexes.first();
        BtreeIndex bti = rt.getBtreeIndex(index);
        BtreeIndex.Iter iter = bti.iter(rt).next();
        int i = 0;
        long first = 0;
        long last = 0;
        Transaction wt = theDB.readwriteTran();
        int tblnum = wt.ck_getTable(tablename).num;
        for (; !iter.eof(); iter.next()) {
            Record r = rt.input(iter.keyadr());
            if (squeeze) r = DbDump.squeezeRecord(r, fields);
            last = Data.outputRecordForCompact(wt, tblnum, r);
            if (first == 0) first = last;
            if (++i % 100 == 0) {
                wt.ck_complete();
                wt = theDB.readwriteTran();
            }
        }
        if (first != 0) createIndexes(wt, tblnum, first - 4, last - 4);
        wt.ck_complete();
    }
