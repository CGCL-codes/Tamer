    public static void renameTable(Database db, String oldname, String newname) {
        if (oldname.equals(newname)) return;
        checkForSystemTable(oldname, "rename");
        Transaction tran = db.readwriteTran();
        try {
            if (tran.tableExists(newname)) throw new SuException("rename table: table already exists: " + newname);
            Table table = tran.ck_getTable(oldname);
            TableData td = tran.getTableData(table.num);
            Data.update_any_record(tran, "tables", "table", key(table.num), Table.record(newname, table.num, td.nextfield, td.nrecords, td.totalsize));
            tran.updateTable(table.num);
            tran.ck_complete();
        } finally {
            tran.abortIfNotComplete();
        }
    }
