    protected ResultSet executeQuery(PreparedStatement ps) throws SQLException {
        long ts = System.currentTimeMillis();
        ResultSet rs = ps.executeQuery();
        long executeTimeInMs = System.currentTimeMillis() - ts;
        context.incrementStat(executeTimeInMs, ChannelRouterContext.STAT_QUERY_TIME_MS);
        if (executeTimeInMs > Constants.LONG_OPERATION_THRESHOLD) {
            log.warn("Selected data to route in {} ms for {}", executeTimeInMs, context.getChannel().getChannelId());
        } else if (log.isDebugEnabled()) {
            log.debug("Selected data to route in {} ms for {}", executeTimeInMs, context.getChannel().getChannelId());
        }
        return rs;
    }
