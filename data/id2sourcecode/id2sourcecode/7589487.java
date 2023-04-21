    public static boolean ensureTable(Database db, String tablename) {
        int tblnum = db.getNextTableNum();
        Transaction tran = db.readwriteTran();
        try {
            if (tran.tableExists(tablename)) return false;
            Record r = Table.record(tablename, tblnum, 0, 0);
            Data.add_any_record(tran, "tables", r);
            tran.updateTable(tblnum);
            tran.ck_complete();
        } finally {
            tran.abortIfNotComplete();
        }
        return true;
    }
