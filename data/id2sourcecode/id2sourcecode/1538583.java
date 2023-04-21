    public synchronized void readLock(long offset) {
        notEnded();
        Transaction writer = trans.readLock(this, offset);
        if (writer != null) {
            if (this.inConflict || writer.outConflict) abortThrow("read-write conflict with " + writer);
            writer.inConflict = true;
            this.outConflict = true;
        }
        Set<Transaction> writes = trans.writes(offset);
        for (Transaction w : writes) {
            if (w == this || w.commitTime < asof) continue;
            if (w.outConflict) abortThrow("read-write conflict with " + w);
            this.outConflict = true;
        }
        for (Transaction w : writes) w.inConflict = true;
    }
