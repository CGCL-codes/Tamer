    public static boolean ensureIndex(Database db, String tablename, String columns, boolean isKey, boolean unique, boolean lower, String fktablename, String fkcolumns, int fkmode) {
        if (fkcolumns == null || fkcolumns.equals("")) fkcolumns = columns;
        synchronized (db.commitLock) {
            Transaction tran = db.readwriteTran();
            try {
                Table table = tran.ck_getTable(tablename);
                ImmutableList<Integer> colnums = table.columns.nums(columns);
                if (table.hasIndex(columns)) return false;
                BtreeIndex btreeIndex = new BtreeIndex(db.dest, table.num, columns, isKey, unique, fktablename, fkcolumns, fkmode);
                Data.add_any_record(tran, "indexes", btreeIndex.record);
                List<BtreeIndex> btis = new ArrayList<BtreeIndex>();
                tran.updateTable(table.num, btis);
                Table fktable = null;
                if (fktablename != null) {
                    fktable = tran.getTable(fktablename);
                    if (fktable != null) tran.updateTable(fktable.num);
                }
                insertExistingRecords(db, tran, columns, table, colnums, fktablename, fktable, fkcolumns, btreeIndex);
                for (BtreeIndex bti : btis) if (bti.columns.equals(columns)) tran.addBtreeIndex(bti);
                tran.ck_complete();
            } catch (RuntimeException e) {
                throw e;
            } finally {
                tran.abortIfNotComplete();
            }
            return true;
        }
    }
