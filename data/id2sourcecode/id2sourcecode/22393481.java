    protected static String fullRefresh(final Connection masterConn, final Connection replicaConn, final Subscriber suber, final Subscription subs, final boolean concurrent) throws SQLException, SyncDatabaseException {
        if (masterConn == null || replicaConn == null || subs == null) {
            throw new SyncDatabaseException("error.argument");
        }
        FullReader reader = null;
        FullWriter writer = null;
        long count = 0;
        try {
            SyncDatabaseDAO.truncate(replicaConn, subs.getReplicaTable(), concurrent);
            reader = new FullReader(masterConn, subs.getQuery());
            writer = new FullWriter(replicaConn, subs.getSchema(), subs.getTable(), reader.getColumnCount());
            MappingData.createDataMappers(reader.getColumnMapping(), writer.getColumnMapping());
            writer.prepare(replicaConn, subs.getReplicaTable());
            Object[] columns;
            while ((columns = reader.getNextColumns()) != null) {
                writer.setColumns(columns);
                count++;
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        subs.setLastType("F");
        if (suber != null) {
            suber.setLastType("F");
            suber.setLastCount(count);
        }
        return mProperty.getMessage("info.full", count);
    }
