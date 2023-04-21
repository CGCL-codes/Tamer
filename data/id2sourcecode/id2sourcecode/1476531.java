    public Transaction readwriteTran() {
        synchronized (commitLock) {
            return new Transaction(trans, false, tables, tabledata, btreeIndexes);
        }
    }
