    public static boolean ensureColumn(Database db, String tablename, String column) {
        synchronized (db.commitLock) {
            Transaction tran = db.readwriteTran();
            try {
                Table table = tran.ck_getTable(tablename);
                TableData td = tran.getTableData(table.num);
                int fldnum = isRuleField(column) ? -1 : td.nextfield;
                if (!column.equals("-")) {
                    if (fldnum == -1) column = column.substring(0, 1).toLowerCase() + column.substring(1);
                    if (table.hasColumn(column)) return false;
                    Record rec = Column.record(table.num, column, fldnum);
                    Data.add_any_record(tran, "columns", rec);
                    tran.updateTable(table.num);
                }
                if (fldnum >= 0) tran.updateTableData(td.withField());
                tran.ck_complete();
            } finally {
                tran.abortIfNotComplete();
            }
            return true;
        }
    }
