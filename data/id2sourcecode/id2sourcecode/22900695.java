    private static int dumpDatabaseImp(String filename) throws Throwable {
        FileChannel fout = new FileOutputStream(filename).getChannel();
        try {
            Transaction t = theDB.readonlyTran();
            try {
                writeFileHeader(fout);
                BtreeIndex bti = t.getBtreeIndex(Database.TN.TABLES, "tablename");
                BtreeIndex.Iter iter = bti.iter(t).next();
                int n = 0;
                for (; !iter.eof(); iter.next()) {
                    Record r = t.input(iter.keyadr());
                    String tablename = r.getString(Table.TABLE);
                    if (Schema.isSystemTable(tablename)) continue;
                    dump1(fout, t, tablename, true);
                    ++n;
                }
                dump1(fout, t, "views", true);
                return ++n;
            } finally {
                t.complete();
            }
        } finally {
            fout.close();
        }
    }
