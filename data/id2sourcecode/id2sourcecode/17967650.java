    public void transactionApplied(Transaction.Data xa, boolean rollback) {
        assert Thread.holdsLock(writeLock);
        fieldChanged.clear();
        structureChanged.clear();
        reader.getQueue().restore(xa);
        reader.resetCursor();
        try {
            if (rollback) {
                reader.supplyInverse(this);
            } else {
                reader.supply(this);
            }
        } catch (java.io.IOException e) {
            throw new FatalPersistenceException(e);
        }
        if (!structureChanged.isEmpty()) {
            for (int i = itemMap.size() - 1; i >= 0; i--) {
                itemMap.setValueAt(i, null);
            }
            stamp++;
            new TreeDiff().compare(oldTree, this, this, this, treeMulticaster);
            root.updateChildren(true);
        }
        if (!fieldChanged.isEmpty()) {
            updateFields(root);
        }
    }
