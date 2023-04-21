    public void dumpCurrentTransaction(PrintWriter writer) {
        TransactionImpl tx;
        if (writer == null) throw new IllegalArgumentException("Argument writer is null");
        tx = (TransactionImpl) getTransaction();
        if (tx == null) writer.println("No transaction associated with current thread"); else {
            writer.println("  Transaction " + tx._xid + " " + Util.getStatus(tx._status));
            writer.println("  Started " + Util.fromClock(tx._started) + " time-out " + Util.fromClock(tx._timeout));
        }
    }
