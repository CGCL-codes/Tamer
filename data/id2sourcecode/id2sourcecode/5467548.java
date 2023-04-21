    private void writeCurrentRequests(JavaInformations javaInformations, Map<String, HtmlCounterReport> counterReportsByCounterName) throws IOException {
        final List<ThreadInformations> threadInformationsList = javaInformations.getThreadInformationsList();
        final boolean stackTraceEnabled = javaInformations.isStackTraceEnabled();
        writeCurrentRequests(threadInformationsList, stackTraceEnabled, MAX_CURRENT_REQUESTS_DISPLAYED_IN_MAIN_REPORT, false, counterReportsByCounterName);
    }
