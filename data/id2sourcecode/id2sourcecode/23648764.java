    public void sampleOccurred(SampleEvent e) {
        if (isWriteResults()) {
            AssertionResult[] assertionResults = e.getResult().getAssertionResults();
            String label = e.getResult().getSampleLabel(true);
            Date startTime = new Date(e.getResult().getStartTime());
            String resultFileName = null;
            String thread = e.getThreadGroup();
            StringBuffer messages = new StringBuffer();
            for (int j = 0; j < assertionResults.length; j++) {
                if (assertionResults[j] instanceof SnmpAssertionResult) {
                    SnmpAssertionResult result = (SnmpAssertionResult) assertionResults[j];
                    if (result.isFailure() || result.isError()) {
                        if (resultFileName == null) {
                            resultFileName = writeResult(result, thread, label, startTime);
                        }
                        result.setErrorUrl(getBaseUrl() + "/" + resultFileName);
                    }
                    messages.append(extractMessage(result));
                }
            }
            if (resultFileName != null) {
                writeSummary(thread, label, startTime, resultFileName, messages.toString());
            }
        }
        super.sampleOccurred(e);
    }
