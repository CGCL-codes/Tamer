    private void dumpPool() {
        final BlockingQueue<List<Pool>> queue = new ArrayBlockingQueue<List<Pool>>(capacity);
        final String consoleId = "dumpPool";
        final Thread[] threads = new Thread[2];
        threads[0] = new Thread("poolDumpReaderThread") {

            @Override
            public void run() {
                Thread writerThread = threads[1];
                PoolDao poolDao = new PoolDao(BaseDao.ConnType.DUMP);
                int next = 1, progress = 0, tmp = 0;
                Paging paging = new Paging(pageSize);
                List<Pool> list = null;
                boolean offer = false;
                do {
                    paging.setPageNumber(next);
                    try {
                        list = poolDao.listFullPools(paging);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        return;
                    }
                    tmp = Math.round(paging.getPageNumber() * 1f / paging.getPageTotal() * 100);
                    if (progress != tmp) {
                        progress = tmp;
                        console.flushablePrint(consoleId, "Pool progress: " + progress + "%");
                    }
                    offer = false;
                    do {
                        try {
                            offer = queue.offer(list, 5, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                        }
                        if (!writerThread.isAlive()) {
                            return;
                        }
                    } while (!offer);
                    if (paging.hasNext()) {
                        next++;
                    }
                } while (paging.hasNext());
            }
        };
        threads[1] = new Thread("poolDumpWriterThread") {

            @Override
            public void run() {
                Thread readerThread = threads[0];
                PoolDao poolDao = new PoolDao();
                List<Pool> list = null;
                while (!queue.isEmpty() || readerThread.isAlive()) {
                    list = null;
                    do {
                        try {
                            list = queue.poll(5, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                        }
                        if (list == null && !readerThread.isAlive()) {
                            break;
                        }
                    } while (list == null);
                    try {
                        for (Pool pool : list) {
                            poolDao.insertPool(pool);
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                        countDownLatch.countDown();
                        return;
                    }
                }
                countDownLatch.countDown();
                console.remove(consoleId);
                console.println("Pool table dump finish!\t\t\t\t");
            }
        };
        console.println("Pool table dump start.");
        for (Thread thread : threads) {
            thread.start();
        }
    }
