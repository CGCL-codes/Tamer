    private void printCSVResults() {
        String lineSeparator = ",";
        writer.println();
        writer.print("Testcase" + lineSeparator);
        for (int iThread = 1; iThread <= sessions; iThread++) {
            writer.print("Thread " + iThread + " (ms)" + lineSeparator);
        }
        writer.println("AVG (ms)");
        Iterator<Entry<String, LinkedHashMap<String, Result>>> allTests = allTestResults.entrySet().iterator();
        while (allTests.hasNext()) {
            Entry<String, LinkedHashMap<String, Result>> testEntry = allTests.next();
            writer.print(testEntry.getKey() + lineSeparator);
            long overallMilliSeconds = 0;
            Iterator<Result> results = testEntry.getValue().values().iterator();
            while (results.hasNext()) {
                Result result = results.next();
                overallMilliSeconds += result.getDuration();
                writer.print(result.getDuration() + lineSeparator);
            }
            writer.println(overallMilliSeconds / sessions);
        }
    }
