    public final void commitXA(boolean activateItems) {
        assert Thread.holdsLock(writeLock);
        if (activateItems && (xaCount == 1) && (currentXA != null)) {
            activateItems();
        }
        if ((--xaCount == 0) && (currentXA != null)) {
            currentXA.commit();
            currentXA = null;
            runAfterCommit();
        }
    }
