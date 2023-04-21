    private void writeTestResults(TestResult r, Test test) {
        if ("Error".equals(r.testState)) {
            numError++;
        } else if ("Failure".equals(r.testState)) {
            numFailed++;
        } else if ("Pass".equals(r.testState)) {
            numPassed++;
        }
        totalTests++;
        try {
            synchronized (this.getClass()) {
                timingsWriter.write(test.getClass().getName() + ", ");
                timingsWriter.write(((test instanceof TestCase) ? ((TestCase) test).getName() : "") + ", ");
                timingsWriter.write(Thread.currentThread().getName() + ", ");
                timingsWriter.write(r.testState + ", ");
                timingsWriter.write((((float) r.testTime) / 1000000f) + ", ");
                timingsWriter.write((r.testEndMem - r.testStartMem) + ", ");
                timingsWriter.write(r.testConcurrency + ", ");
                timingsWriter.write(r.testParam + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to write out test results: " + e, e);
        }
    }
