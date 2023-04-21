    public static void renameColumn(Database db, String tablename, String oldname, String newname) {
        if (oldname.equals(newname)) return;
        if (isSystemColumn(tablename, oldname)) throw new SuException("rename column: can't rename system column: " + oldname + " in " + tablename);
        synchronized (db.commitLock) {
            Transaction tran = db.readwriteTran();
            try {
                Table table = tran.ck_getTable(tablename);
                if (table.hasColumn(newname)) throw new SuException("rename column: column already exists: " + newname + " in " + tablename);
                Column col = table.getColumn(oldname);
                if (col == null) throw new SuException("rename column: nonexistent column: " + oldname + " in " + tablename);
                Data.update_any_record(tran, "columns", "table,column", key(table.num, oldname), Column.record(table.num, newname, col.num));
                for (Index index : table.indexes) {
                    List<String> cols = commasToList(index.columns);
                    int i = cols.indexOf(oldname);
                    if (i < 0) continue;
                    cols.set(i, newname);
                    String newColumns = listToCommas(cols);
                    Record newRecord = tran.getBtreeIndex(index).withColumns(newColumns);
                    Data.update_any_record(tran, "indexes", "table,columns", key(table.num, index.columns), newRecord);
                }
                List<BtreeIndex> btis = new ArrayList<BtreeIndex>();
                tran.updateTable(table.num, btis);
                for (BtreeIndex bti : btis) tran.addBtreeIndex(bti);
                tran.ck_complete();
            } finally {
                tran.abortIfNotComplete();
            }
        }
    }
