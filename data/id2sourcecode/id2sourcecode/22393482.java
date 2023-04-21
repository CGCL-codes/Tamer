    protected static String incrementalRefresh(final Connection masterConn, final Connection replicaConn, final Subscriber suber, final Subscription subs) throws SQLException, SyncDatabaseException {
        if (masterConn == null || replicaConn == null || suber == null || subs == null) {
            throw new SyncDatabaseException("error.argument");
        }
        IncrementalReader reader = null;
        IncrementalWriter writer = null;
        try {
            Hashtable<Short, String> pkNames = SyncDatabaseDAO.getPKNames(masterConn, suber.getNspName(), suber.getRelName());
            final String incrementalQuery = IncrementalReader.getIncrementalQuery(suber.getMlogName(), subs.getQuery(), pkNames, suber.getLastMlogID());
            reader = new IncrementalReader(masterConn, incrementalQuery, pkNames.size());
            pkNames = SyncDatabaseDAO.getPKNames(replicaConn, subs.getSchema(), subs.getTable());
            writer = new IncrementalWriter(replicaConn, subs.getSchema(), subs.getTable(), reader.getColumnCount(), pkNames);
            MappingData.createDataMappers(reader.getColumnMapping(), writer.getColumnMapping());
            writer.prepare(replicaConn, subs.getReplicaTable());
            Object[] columns;
            while ((columns = reader.getNextColumns()) != null) {
                writer.setColumns(columns);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        subs.setLastType("I");
        suber.setLastType("I");
        suber.setLastCount(suber.getLastCount() + writer.getInsertCount() - writer.getDeleteCount());
        log.debug("INSERT:" + writer.getInsertCount() + "/DELETE:" + writer.getDeleteCount() + "/UPDATE:" + writer.getUpdateCount() + "/PK COUNT:" + writer.getExecCount());
        return mProperty.getMessage("info.incremental", writer.getInsertCount(), writer.getUpdateCount(), writer.getDeleteCount());
    }
