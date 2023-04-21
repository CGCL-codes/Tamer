            public Integer doInConnection(Connection c) throws SQLException, DataAccessException {
                int dataCount = 0;
                PreparedStatement ps = null;
                ResultSet rs = null;
                boolean autoCommit = c.getAutoCommit();
                try {
                    if (requiresAutoCommitFalse) {
                        c.setAutoCommit(false);
                    }
                    String channelId = context.getChannel().getChannelId();
                    ps = prepareStatment(c);
                    long ts = System.currentTimeMillis();
                    rs = ps.executeQuery();
                    long executeTimeInMs = System.currentTimeMillis() - ts;
                    context.incrementStat(executeTimeInMs, ChannelRouterContext.STAT_QUERY_TIME_MS);
                    if (executeTimeInMs > Constants.LONG_OPERATION_THRESHOLD) {
                        log.warn("RoutedDataSelectedInTime", executeTimeInMs, channelId);
                    }
                    int toRead = maxQueueSize - dataQueue.size();
                    List<Data> memQueue = new ArrayList<Data>(toRead);
                    ts = System.currentTimeMillis();
                    while (rs.next() && reading) {
                        if (StringUtils.isBlank(rs.getString(13))) {
                            Data data = dataService.readData(rs);
                            context.setLastDataIdForTransactionId(data);
                            memQueue.add(data);
                            dataCount++;
                            context.incrementStat(System.currentTimeMillis() - ts, ChannelRouterContext.STAT_READ_DATA_MS);
                        } else {
                            context.incrementStat(System.currentTimeMillis() - ts, ChannelRouterContext.STAT_REREAD_DATA_MS);
                        }
                        ts = System.currentTimeMillis();
                        if (toRead == 0) {
                            copyToQueue(memQueue);
                            toRead = maxQueueSize - dataQueue.size();
                            memQueue = new ArrayList<Data>(toRead);
                        } else {
                            toRead--;
                        }
                        context.incrementStat(System.currentTimeMillis() - ts, ChannelRouterContext.STAT_ENQUEUE_DATA_MS);
                        ts = System.currentTimeMillis();
                    }
                    ts = System.currentTimeMillis();
                    copyToQueue(memQueue);
                    context.incrementStat(System.currentTimeMillis() - ts, ChannelRouterContext.STAT_ENQUEUE_DATA_MS);
                    return dataCount;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeStatement(ps);
                    rs = null;
                    ps = null;
                    if (requiresAutoCommitFalse) {
                        c.commit();
                        c.setAutoCommit(autoCommit);
                    }
                    boolean done = false;
                    do {
                        done = dataQueue.offer(new EOD());
                        if (!done) {
                            AppUtils.sleep(50);
                        }
                    } while (!done && reading);
                    reading = false;
                }
            }
