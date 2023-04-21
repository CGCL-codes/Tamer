    private void collectGarbageNoLock(boolean memGC) {
        if (readOnly) return;
        SnapshotImpl current;
        ArrayList<WeakReference<SnapshotImpl>> snapshots;
        synchronized (this) {
            current = immutableSnapshot();
            snapshots = new ArrayList(activeSnapshots);
        }
        if (memGC) {
            boolean needMemGC = false;
            for (int i = 0; i < snapshots.size(); i++) {
                SnapshotImpl snapshot = snapshots.get(i).get();
                if (snapshot != null && !snapshot.equals(current) && !(snapshot == cachedSnapshot && snapshot.references == 1)) {
                    if (debug) System.out.println("Need mem GC, snapshot open: " + snapshot);
                    needMemGC = true;
                    break;
                }
            }
            if (needMemGC) System.gc();
        }
        current.forget();
        BitSet whiteList = new BitSet();
        synchronized (this) {
            if (writingThread != Thread.currentThread()) throw new InternalSmyleError("Must have write lock");
        }
        for (int i = 0; i < snapshots.size(); i++) {
            SnapshotImpl snapshot = snapshots.get(i).get();
            if (snapshot != null) snapshot.collectChunks(whiteList);
        }
        chunkManager.deleteEverythingBut(whiteList);
    }
