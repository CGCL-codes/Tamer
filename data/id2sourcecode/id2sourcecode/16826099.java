    public ISqlReadCursor<Data> selectDataFor(Batch batch) {
        return sqlTemplate.queryForCursor(getDataSelectSql(batch.getBatchId(), -1l, batch.getChannelId()), dataMapper, new Object[] { batch.getBatchId() }, new int[] { Types.NUMERIC });
    }
