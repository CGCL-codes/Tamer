    public TestResult doRun(Test suite, boolean wait) throws Exception {
        StatsStore.open(jdbcDriver, connectionURL);
        TestResult result = new TestResult();
        result.addListener(fPrinter);
        result.addListener(fPerfStatCollector);
        long startTime = System.currentTimeMillis();
        StatsStore.now = startTime;
        suite.run(result);
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        fPrinter.print(result, runTime);
        fPerfStatCollector.digest();
        StatsStore.close();
        pause(wait);
        return result;
    }
