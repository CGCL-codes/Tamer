    public synchronized void writeLock(long offset) {
        notEnded();
        Set<Transaction> readers = trans.writeLock(this, offset);
        if (readers == null) abortThrow("write-write conflict");
        for (Transaction reader : readers) if (reader.isActive() || reader.committedAfter(this)) {
            if (reader.inConflict || this.outConflict) abortThrow("write-read conflict with " + reader);
            this.inConflict = true;
        }
        for (Transaction reader : readers) reader.outConflict = true;
    }
