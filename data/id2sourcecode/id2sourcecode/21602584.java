    private Map<String, String> processResults(List<Stresser> stressers) {
        long duration = 0;
        int reads = 0;
        int writes = 0;
        int failures = 0;
        long readsDurations = 0;
        long writesDurations = 0;
        for (Stresser stresser : stressers) {
            duration += stresser.totalDuration();
            readsDurations += stresser.readDuration;
            writesDurations += stresser.writeDuration;
            reads += stresser.reads;
            writes += stresser.writes;
            failures += stresser.nrFailures;
        }
        Map<String, String> results = new LinkedHashMap<String, String>();
        results.put("DURATION", str(duration));
        double requestPerSec = (reads + writes) / ((duration / numOfThreads) / 1000.0);
        results.put("REQ_PER_SEC", str(requestPerSec));
        results.put("READS_PER_SEC", str(reads / ((readsDurations / numOfThreads) / 1000.0)));
        results.put("WRITES_PER_SEC", str(writes / ((writesDurations / numOfThreads) / 1000.0)));
        results.put("READ_COUNT", str(reads));
        results.put("WRITE_COUNT", str(writes));
        results.put("FAILURES", str(failures));
        log.info("Finished generating report. Nr of failed operations on this node is: " + failures + ". Test duration is: " + Utils.getDurationString(System.currentTimeMillis() - startTime));
        return results;
    }
