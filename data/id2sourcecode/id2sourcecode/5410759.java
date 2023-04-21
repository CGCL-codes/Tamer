    @Override
    public void writeInsertValues(DataWrite dp, Channel obj) throws BasicException {
        dp.setString(1, obj.getChannelId());
        dp.setInt(2, obj.getProcessingOrder());
        dp.setInt(3, obj.getMaxBatchSize());
        dp.setInt(4, obj.getMaxBatchToSend());
        dp.setInt(5, obj.getMaxDataToRoute());
        dp.setInt(6, (int) obj.getExtractPeriodMillis());
        dp.setBoolean(7, obj.isEnabled());
        dp.setBoolean(8, obj.isUseOldDataToRoute());
        dp.setBoolean(9, obj.isUseRowDataToRoute());
        dp.setBoolean(10, obj.isUsePkDataToRoute());
        dp.setBoolean(11, obj.isContainsBigLob());
        dp.setString(12, obj.getBatchAlgorithm());
    }
