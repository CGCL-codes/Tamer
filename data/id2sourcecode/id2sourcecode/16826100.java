    protected String getDataSelectSql(long batchId, long startDataId, String channelId) {
        String startAtDataIdSql = startDataId >= 0l ? " and d.data_id >= ? " : "";
        return symmetricDialect.massageDataExtractionSql(getSql("selectEventDataToExtractSql", startAtDataIdSql, " order by d.data_id asc"), configurationService.getNodeChannel(channelId, false).getChannel());
    }
