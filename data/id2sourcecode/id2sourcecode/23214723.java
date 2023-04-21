    private static void runBenchmarkWithZipfDistributionReadWriteLock(ReadWriteLock rwlock, int thrdnum, Status stat, Segments pager, ConcurrentPluggableCache<Long, byte[]> cache, int capacity, int round, double percent, int scanLength, long[] dist) {
        boolean scanning = false;
        long scanCount = 0L;
        final Random rand = new Random(129);
        int scanned = 0, zipf = 0;
        long base = 0L;
        long mills1 = System.currentTimeMillis();
        long ops = 0L;
        long miss = 0L;
        int iocontention = 0;
        final Lock readLock = rwlock.readLock();
        final Lock writeLock = rwlock.writeLock();
        final int start = thrdnum * round;
        final int limit = start + round - 1;
        for (int nth = start, i = 0; !_stop; nth = (nth < limit) ? nth + 1 : start, i++) {
            final long key;
            if (scanning || scanLength != 0 && (i == scanLength && rand.nextDouble() < percent)) {
                if (++scanCount <= scanLength) {
                    if (!scanning) {
                        base = dist[nth];
                    }
                    key = base + scanCount;
                    scanning = true;
                    scanned++;
                } else {
                    key = dist[nth];
                    scanCount = 0;
                    scanning = false;
                    i = 0;
                    zipf++;
                }
            } else {
                key = dist[nth];
                zipf++;
            }
            final ICacheEntry<Long, byte[]> entry = cache.allocateEntry(key, readLock, writeLock);
            if (volatilesync) {
                if (entry.volatileGetValue() == null) {
                    synchronized (entry) {
                        if (entry.getValue() == null) {
                            byte[] b = emurateReadInPage(pager, capacity, key);
                            entry.setValue(b);
                            miss++;
                        }
                    }
                }
            } else {
                if (entry.getValue() == null) {
                    final byte[] b = emurateReadInPage(pager, capacity, key);
                    if (!entry.compareAndSetValue(null, b)) {
                        iocontention++;
                    }
                    miss++;
                }
            }
            entry.unpin();
            ops++;
        }
        long mills2 = System.currentTimeMillis();
        stat.mills = mills2 - mills1;
        stat.ops = ops;
        stat.miss = miss;
        stat.iocontention = iocontention;
        if (showstdout) {
            System.out.println("[" + Thread.currentThread().getName() + "] scanned: " + scanned + ", zipf: " + zipf);
        }
    }
