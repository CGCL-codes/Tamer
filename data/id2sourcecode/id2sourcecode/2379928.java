    private void doHtmlPart(HttpServletRequest httpRequest, String part, HtmlReport htmlReport) throws IOException {
        if (GRAPH_PART.equalsIgnoreCase(part)) {
            final String graphName = httpRequest.getParameter(GRAPH_PARAMETER);
            htmlReport.writeRequestAndGraphDetail(graphName);
        } else if (USAGES_PART.equalsIgnoreCase(part)) {
            final String graphName = httpRequest.getParameter(GRAPH_PARAMETER);
            htmlReport.writeRequestUsages(graphName);
        } else if (CURRENT_REQUESTS_PART.equalsIgnoreCase(part)) {
            final boolean withoutHeaders = HTML_BODY_FORMAT.equalsIgnoreCase(httpRequest.getParameter(FORMAT_PARAMETER));
            doCurrentRequests(htmlReport, withoutHeaders);
        } else if (THREADS_PART.equalsIgnoreCase(part)) {
            htmlReport.writeAllThreadsAsPart();
        } else if (COUNTER_SUMMARY_PER_CLASS_PART.equalsIgnoreCase(part)) {
            final String counterName = httpRequest.getParameter(COUNTER_PARAMETER);
            final String requestId = httpRequest.getParameter(GRAPH_PARAMETER);
            htmlReport.writeCounterSummaryPerClass(counterName, requestId);
        } else {
            doHtmlPartForSystemActions(httpRequest, part, htmlReport);
        }
    }
