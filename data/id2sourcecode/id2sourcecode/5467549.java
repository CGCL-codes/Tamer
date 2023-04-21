    void writeAllCurrentRequestsAsPart(boolean onlyTitleAndDetails) throws IOException {
        assert javaInformationsList.size() == 1;
        final List<ThreadInformations> threadInformationsList = javaInformationsList.get(0).getThreadInformationsList();
        final boolean stackTraceEnabled = javaInformationsList.get(0).isStackTraceEnabled();
        writeCurrentRequests(threadInformationsList, stackTraceEnabled, Integer.MAX_VALUE, onlyTitleAndDetails, null);
    }
