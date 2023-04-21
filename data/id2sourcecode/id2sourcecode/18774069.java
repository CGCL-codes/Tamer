    protected List<DetailRecord> getChartData(ChartDetailBulkQuery chartDetailBulkQuery) throws IOException {
        final String intervalDesc = chartDetailBulkQuery.getInterval().getDescription();
        final long queryId = chartDetailBulkQuery.getQueryId();
        final List<DetailRecord> result = new ArrayList<DetailRecord>();
        final Set<DaytonaDetailQuery> dqueries = new HashSet<DaytonaDetailQuery>();
        for (final InputChannelItemInterface channelitem : chartDetailBulkQuery.getChannelList()) {
            if (channelitem instanceof InputChannelInterface) {
                dqueries.add(daytonaDetailQueries.get(channelitem));
            }
        }
        try {
            final String[] querystring = new String[] { Integer.toString((int) (chartDetailBulkQuery.getBeginDate().getTime() / 1000)), Integer.toString((int) (chartDetailBulkQuery.getEndDate().getTime() / 1000)), makeCommaList(chartDetailBulkQuery.getSourceList()) };
            final ThreadGroup tg = new ThreadGroup("Detail worker threads") {

                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    final DetailWorkerThread dwt = (DetailWorkerThread) t;
                    dwt.setErrorStatus();
                }
            };
            final List<DetailWorkerThread> threads = new ArrayList<DetailWorkerThread>(dqueries.size());
            for (final DaytonaDetailQuery dsq : dqueries) {
                final String workerName = "DetailWorkerThread " + dsq.getName();
                final DetailWorkerThread t = new DetailWorkerThread(tg, workerName, dsq, querystring, queryId);
                threads.add(t);
                t.start();
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
