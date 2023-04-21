    private void writeCurrentRequests(List<ThreadInformations> threadInformationsList, boolean stackTraceEnabled, int maxContextsDisplayed, boolean onlyTitleAndDetails, Map<String, HtmlCounterReport> counterReportsByCounterName) throws IOException {
        final List<CounterRequestContext> rootCurrentContexts = collector.getRootCurrentContexts();
        final HtmlCounterRequestContextReport htmlCounterRequestContextReport = new HtmlCounterRequestContextReport(rootCurrentContexts, counterReportsByCounterName, threadInformationsList, stackTraceEnabled, maxContextsDisplayed, writer);
        if (onlyTitleAndDetails) {
            htmlCounterRequestContextReport.writeTitleAndDetails();
        } else {
            htmlCounterRequestContextReport.toHtml();
        }
    }
