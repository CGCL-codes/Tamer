    @Override
    public void writeInsertValues(DataWrite dp, NodeChannelCtl obj) throws BasicException {
        dp.setString(1, obj.getNodeId());
        dp.setString(2, obj.getChannelId());
        dp.setBoolean(3, obj.isSuspendEnabled());
        dp.setBoolean(4, obj.isIgnoreEnabled());
        dp.setTimestamp(5, obj.getLastExtractTime());
    }
