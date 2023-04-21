    public static void removeColumn(Database db, String tablename, String name) {
        if (isSystemColumn(tablename, name)) throw new SuException("delete column: can't delete system column: " + name + " from " + tablename);
        synchronized (db.commitLock) {
            Transaction tran = db.readwriteTran();
            try {
                Table table = tran.ck_getTable(tablename);
                if (table.columns.find(name) == null) throw new SuException("delete column: nonexistent column: " + name + " in " + tablename);
                for (Index index : table.indexes) if (index.hasColumn(name)) throw new SuException("delete column: can't delete column used in index: " + name + " in " + tablename);
                removeColumn(db, tran, table, name);
                tran.updateTable(table.num);
                tran.ck_complete();
            } finally {
                tran.abortIfNotComplete();
            }
        }
    }
