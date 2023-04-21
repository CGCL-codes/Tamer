    protected long insertData(ISqlTransaction transaction, final Data data) {
        long id = transaction.insertWithGeneratedKey(getSql("insertIntoDataSql"), symmetricDialect.getSequenceKeyName(SequenceIdentifier.DATA), symmetricDialect.getSequenceName(SequenceIdentifier.DATA), data.getTableName(), data.getDataEventType().getCode(), data.getRowData(), data.getPkData(), data.getOldData(), data.getTriggerHistory() != null ? data.getTriggerHistory().getTriggerHistoryId() : -1, data.getChannelId());
        data.setDataId(id);
        return id;
    }
