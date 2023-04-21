    @Override
    public void writeInsertValues(DataWrite dp, Trigger obj) throws BasicException {
        dp.setString(1, obj.getTriggerId());
        dp.setString(2, obj.getSourceCatalogName());
        dp.setString(3, obj.getSourceSchemaName());
        dp.setString(4, obj.getSourceTableName());
        dp.setString(5, obj.getChannelId());
        dp.setBoolean(6, obj.isSyncOnUpdate());
        dp.setBoolean(7, obj.isSyncOnInsert());
        dp.setBoolean(8, obj.isSyncOnDelete());
        dp.setBoolean(9, obj.isSyncOnIncomingBatch());
        dp.setString(10, obj.getNameForUpdateTrigger());
        dp.setString(11, obj.getNameForInsertTrigger());
        dp.setString(12, obj.getNameForDeleteTrigger());
        dp.setString(13, obj.getSyncOnUpdateCondition());
        dp.setString(14, obj.getSyncOnInsertCondition());
        dp.setString(15, obj.getSyncOnDeleteCondition());
        dp.setString(16, obj.getExternalSelect());
        dp.setString(17, obj.getTxIdExpression());
        dp.setString(18, obj.getExcludedColumnNames());
        dp.setTimestamp(19, obj.getCreateTime());
        dp.setString(20, obj.getLastUpdateBy());
        dp.setTimestamp(21, obj.getLastUpdateTime());
    }
