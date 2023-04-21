    public void insertOutgoingBatch(ISqlTransaction transaction, OutgoingBatch outgoingBatch) {
        outgoingBatch.setLastUpdatedHostName(AppUtils.getServerId());
        long batchId = transaction.insertWithGeneratedKey(getSql("insertOutgoingBatchSql"), symmetricDialect.getSequenceKeyName(SequenceIdentifier.OUTGOING_BATCH), symmetricDialect.getSequenceName(SequenceIdentifier.OUTGOING_BATCH), outgoingBatch.getNodeId(), outgoingBatch.getChannelId(), outgoingBatch.getStatus().name(), outgoingBatch.isLoadFlag() ? 1 : 0, outgoingBatch.getReloadEventCount(), outgoingBatch.getOtherEventCount(), outgoingBatch.getLastUpdatedHostName());
        outgoingBatch.setBatchId(batchId);
    }
