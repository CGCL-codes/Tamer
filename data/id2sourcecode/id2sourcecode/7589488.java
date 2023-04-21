    public static boolean removeTable(Database db, String tablename) {
        checkForSystemTable(tablename, "drop");
        Transaction tran = db.readwriteTran();
        try {
            if (tran.getView(tablename) != null) {
                tran.removeView(tablename);
            } else {
                Table table = tran.getTable(tablename);
                if (table == null) return false;
                for (Index index : table.indexes) removeIndex(db, tran, table, index.columns);
                for (Column column : table.columns) removeColumn(db, tran, table, column.name);
                Data.remove_any_record(tran, "tables", "table", key(table.num));
                tran.deleteTable(table);
            }
            tran.ck_complete();
        } finally {
            tran.abortIfNotComplete();
        }
        return true;
    }
