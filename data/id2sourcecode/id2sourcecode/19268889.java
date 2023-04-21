    private void printStandardOutResults() {
        String lineSeparator = System.getProperty("line.separator");
        writer.println();
        writer.println("RESULTS:");
        writer.println();
        Iterator<Entry<String, LinkedHashMap<String, Result>>> allTests = allTestResults.entrySet().iterator();
        while (allTests.hasNext()) {
            Entry<String, LinkedHashMap<String, Result>> testEntry = allTests.next();
            String outputMainString = "Test case: " + testEntry.getKey();
            String outputThreadsString = "";
            long overallMilliSeconds = 0;
            Iterator<Result> results = testEntry.getValue().values().iterator();
            while (results.hasNext()) {
                Result result = results.next();
                overallMilliSeconds += result.getDuration();
                String threadString = "    Thread " + result.getTestCaseNumber() + ":";
                for (int i = threadString.length(); i < (iOverallCharacters - String.valueOf(result.getDuration()).length()); i++) {
                    threadString += " ";
                }
                threadString = threadString + result.getDuration() + " ms" + lineSeparator;
                outputThreadsString += threadString;
            }
            for (int i = outputMainString.length(); i < (iOverallCharacters - String.valueOf(overallMilliSeconds / sessions).length()); i++) {
                outputMainString += " ";
            }
            writer.print(outputMainString + (overallMilliSeconds / sessions) + " ms (avg)" + lineSeparator + outputThreadsString);
            writer.println();
        }
    }
