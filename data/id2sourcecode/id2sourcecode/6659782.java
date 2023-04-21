    private void writeToSpace(GraphTask t) {
        Entry entry = t.entry;
        if (entry instanceof RdTSAdapterEntry) {
            RdTSAdapterEntry e = (RdTSAdapterEntry) entry;
            JavaSpace space = TSAdapter.getAdapter().getSpace();
            AccessType atype = t.isDeleting() ? AccessType.Delete : AccessType.Read;
            Entry[] results = null;
            if (e.kind == ReadType.NOTIFY) {
                results = new Entry[1];
                Set<Set<TripleEntry>> data = new HashSet<Set<TripleEntry>>();
                data.add(t.result);
                results[0] = new DataResultExternal(e.operationID, data, new Boolean(true));
            } else {
                results = new Entry[2];
                results[0] = new RdMetaMEntry(t.result, e.space, new TreeSet<java.net.URI>(), e.timeout != null ? e.timeout.getTimeout() : 0, atype, e.clientInfo, e.operationID, e.transactionID);
                results[1] = new RdResultDMEntry(t.result, e.space, new TreeSet<URI>(), e.operationID, e.transactionID, true);
            }
            try {
                for (int i = 0; i < results.length; i++) {
                    space.write(results[i], null, Long.MAX_VALUE);
                }
            } catch (Exception ex) {
                logger.error("Could not write to space read results!", ex);
            }
        }
    }
