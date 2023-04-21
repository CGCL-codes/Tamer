    protected void writeResult(final SpaceResult sr) throws RemoteException, TransactionException {
        if (sr != null) {
            if (DEBUG) System.err.println("WorkerThread::writeResult(): writing " + sr.index + " ...");
            m_proxy.write(sr, m_txn, DEF_RES_LEASE);
        }
    }
