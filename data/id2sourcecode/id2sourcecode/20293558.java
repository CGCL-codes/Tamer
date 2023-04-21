            public Integer doInConnection(Connection c) throws SQLException, DataAccessException {
                int dataCount = 0;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    String channelId = context.getChannel().getChannelId();
                    ps = c.prepareStatement(getSql(context.getChannel().getChannel()), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                    ps.setQueryTimeout(queryTimeout);
                    ps.setFetchSize(fetchSize);
                    ps.setString(1, channelId);
                    ps.setLong(2, dataRef.getRefDataId());
                    long executeTimeInMs = System.currentTimeMillis();
                    rs = ps.executeQuery();
                    executeTimeInMs = System.currentTimeMillis() - executeTimeInMs;
                    if (executeTimeInMs > 30000) {
                        log.warn("RoutedDataSelectedInTime", executeTimeInMs, channelId);
                    }
                    int toRead = maxQueueSize - dataQueue.size();
                    List<Data> memQueue = new ArrayList<Data>(toRead);
                    long ts = System.currentTimeMillis();
                    while (rs.next() && reading) {
                        if (rs.getString(13) == null) {
                            Data data = dataService.readData(rs);
                            context.setLastDataIdForTransactionId(data);
                            memQueue.add(data);
                            dataCount++;
                        }
                        context.incrementStat(System.currentTimeMillis() - ts, RouterContext.STAT_READ_DATA_MS);
                        ts = System.currentTimeMillis();
                        if (toRead == 0) {
                            copyToQueue(memQueue);
                            toRead = maxQueueSize - dataQueue.size();
                            memQueue = new ArrayList<Data>(toRead);
                        } else {
                            toRead--;
                        }
                        context.incrementStat(System.currentTimeMillis() - ts, RouterContext.STAT_ENQUEUE_DATA_MS);
                        ts = System.currentTimeMillis();
                    }
                    copyToQueue(memQueue);
                    return dataCount;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeStatement(ps);
                    reading = false;
                    boolean done = false;
                    do {
                        done = dataQueue.offer(new EOD());
                    } while (!done);
                    rs = null;
                    ps = null;
                }
            }
