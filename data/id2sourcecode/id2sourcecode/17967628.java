    public final void beginXA() {
        assert Thread.holdsLock(writeLock);
        if (xaCount++ > 0) {
            return;
        }
        GraphManager g = isActive() ? regGraph : null;
        GraphTransaction xa = (g == null) ? null : (GraphTransaction) g.getTransaction(true);
        if (xa != null) {
            xa.begin(false);
            currentXA = xa;
        }
    }
