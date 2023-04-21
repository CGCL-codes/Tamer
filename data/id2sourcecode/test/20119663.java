    public void insertIncomingBatch(IncomingBatch batch) {
        if (batch.isPersistable()) {
            batch.setLastUpdatedHostName(AppUtils.getServerId());
            batch.setLastUpdatedTime(new Date());
            sqlTemplate.update(getSql("insertIncomingBatchSql"), new Object[] { Long.valueOf(batch.getBatchId()), batch.getNodeId(), batch.getChannelId(), batch.getStatus().name(), batch.getNetworkMillis(), batch.getFilterMillis(), batch.getDatabaseMillis(), batch.getFailedRowNumber(), batch.getFailedLineNumber(), batch.getByteCount(), batch.getStatementCount(), batch.getFallbackInsertCount(), batch.getFallbackUpdateCount(), batch.getIgnoreCount(), batch.getMissingDeleteCount(), batch.getSkipCount(), batch.getSqlState(), batch.getSqlCode(), StringUtils.abbreviate(batch.getSqlMessage(), 1000), batch.getLastUpdatedHostName(), batch.getLastUpdatedTime() });
        }
    }
