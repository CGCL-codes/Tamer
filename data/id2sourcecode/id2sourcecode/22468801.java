    protected List<DetailRecord> getChartData(ChartDetailBulkQuery chartDetailBulkQuery) throws IOException {
        final String intervalDesc = chartDetailBulkQuery.getInterval().getDescription();
        final long queryId = chartDetailBulkQuery.getQueryId();
        final List<DetailRecord> result = new ArrayList<DetailRecord>();
        final List<DetailQueryInterface> detailQueries = chartDetailBulkQuery.getDetailQueries();
        try {
            final ThreadGroup tg = new ThreadGroup("Detail worker threads") {

                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    final DetailWorkerThread dwt = (DetailWorkerThread) t;
                    dwt.setErrorStatus();
                }
            };
            final List<DetailWorkerThread> threads = new ArrayList<DetailWorkerThread>(detailQueries.size());
            final String beginEpoch = Integer.toString((int) (chartDetailBulkQuery.getBeginDate().getTime() / 1000));
            final String endEpoch = Integer.toString((int) (chartDetailBulkQuery.getEndDate().getTime() / 1000));
            for (final DetailQueryInterface query : detailQueries) {
                for (InputChannelItemInterface channel : query.getChannelList()) {
                    final DaytonaDetailQuery dsq = daytonaDetailQueries.get(channel);
                    final String source = query.getSource().getMetaValue(DataServiceDispatcher.ROUTERNAMEFIELD);
                    final String netInterface = query.getSource().getMetaValue(DataServiceDispatcher.INTERFACENAMEFIELD);
                    final String[] querystring = new String[] { beginEpoch, endEpoch, source, netInterface };
                    final String workerName = "DetailWorkerThread " + dsq.getName();
                    final DetailWorkerThread t = new DetailWorkerThread(tg, workerName, dsq, querystring, queryId);
                    threads.add(t);
                    t.start();
                }
            }
            while (true) {
                boolean alive = false;
                for (final DetailWorkerThread t : threads) {
                    if (t.isErrorStatus()) throw new IOException("Detail worker thread failed");
                    if (t.isAlive()) alive = true; else {
                        final List<DetailRecord> r = t.getResult();
                        if (r != null) {
                            result.addAll(r);
                        }
                    }
                }
                if (!alive) break;
                try {
                    Thread.sleep(SPINSLEEP);
                } catch (InterruptedException e) {
                    System.err.println("!! " + Thread.currentThread().getName() + " interrupted.");
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return result;
    }
