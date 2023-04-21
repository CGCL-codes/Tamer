    public void insertDataAndDataEventAndOutgoingBatch(Data data, String nodeId, String routerId, boolean isLoad) {
        ISqlTransaction transaction = null;
        try {
            transaction = sqlTemplate.startSqlTransaction();
            long dataId = insertData(transaction, data);
            insertDataEventAndOutgoingBatch(transaction, dataId, data.getChannelId(), nodeId, data.getDataEventType(), routerId, isLoad);
            transaction.commit();
        } finally {
            close(transaction);
        }
    }
