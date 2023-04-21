    private void handleIndexesRecord(Record indexes_rec) {
        newdb.addIndexEntriesForRebuild(TN.INDEXES, indexes_rec);
        BtreeIndex.rebuildCreate(newdb.dest, indexes_rec);
        reloadTable(indexes_rec.getInt(Index.TBLNUM));
        Transaction tran = newdb.readwriteTran();
        insertExistingRecords(tran, indexes_rec);
        tran.complete();
    }
