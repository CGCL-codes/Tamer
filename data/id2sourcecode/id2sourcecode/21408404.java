    @Override
    protected PreparedStatement prepareStatment(Connection c) throws SQLException {
        DataRef dataRef = dataService.getDataRef();
        String channelId = context.getChannel().getChannelId();
        PreparedStatement ps = c.prepareStatement(getSql(SELECT_DATA_TO_BATCH_SQL, context.getChannel().getChannel()), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setQueryTimeout(queryTimeout);
        ps.setFetchSize(fetchSize);
        ps.setString(1, channelId);
        ps.setLong(2, dataRef.getRefDataId());
        return ps;
    }
