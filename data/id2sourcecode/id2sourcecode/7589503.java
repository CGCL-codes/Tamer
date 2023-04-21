    public static void removeIndex(Database db, String tablename, String columns) {
        if (isSystemIndex(tablename, columns)) throw new SuException("delete index: can't delete system index: " + columns + " from " + tablename);
        synchronized (db.commitLock) {
            Transaction tran = db.readwriteTran();
            try {
                Table table = tran.ck_getTable(tablename);
                if (table.indexes.size() == 1) throw new SuException("delete index: can't delete last index from " + tablename);
                removeIndex(db, tran, table, columns);
                tran.updateTable(table.num);
                tran.ck_complete();
            } finally {
                tran.abortIfNotComplete();
            }
        }
    }
