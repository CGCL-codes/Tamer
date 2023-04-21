    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        NumberFormat doubleFormat = new DecimalFormat();
        doubleFormat.setMaximumFractionDigits(5);
        for (int i = 1; i <= NUMBER; i++) {
            Date start = new Date();
            _readOps = _writeOps = _readMS = _writeMS = _readLines = 0;
            _connection = getConnection();
            BigInteger rank = recCounter(i, 1, 0, new BitSet(_primes.size()), i % DEPTH_SAVE, 0);
            if (i % 10 == 0) System.gc();
            flush();
            _connection.commit();
            _connection.close();
            _log.info("cache size - " + _cache.size());
            _log.info("rank[" + i + "] - " + rank);
            _log.info("total - " + Runtime.getRuntime().totalMemory() / 1000000);
            _log.info("free - " + Runtime.getRuntime().freeMemory() / 1000000);
            _log.info("used - " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000);
            _log.info("time - " + (new Date().getTime() - start.getTime()) + "ms");
            _log.info("Read - " + _readOps + " operations " + _readMS + " ms " + _readLines + " lines " + doubleFormat.format((((double) _readMS) / _readLines)) + " per line");
            _log.info("Write - " + _writeOps + " operations " + _writeMS + " ms");
        }
        _log.info("finished");
        Thread.sleep(120000);
    }
