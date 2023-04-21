    void doHtml(HttpServletRequest httpRequest, HttpServletResponse httpResponse, List<JavaInformations> javaInformationsList) throws IOException {
        final String part = httpRequest.getParameter(PART_PARAMETER);
        if (!isFromCollectorServer() && (part == null || CURRENT_REQUESTS_PART.equalsIgnoreCase(part) || GRAPH_PART.equalsIgnoreCase(part) || COUNTER_SUMMARY_PER_CLASS_PART.equalsIgnoreCase(part))) {
            collector.collectLocalContextWithoutErrors();
        }
        httpResponse.setContentType(HTML_CONTENT_TYPE);
        final BufferedWriter writer = getWriter(httpResponse);
        try {
            final Range range = httpCookieManager.getRange(httpRequest, httpResponse);
            final HtmlReport htmlReport = new HtmlReport(collector, collectorServer, javaInformationsList, range, writer);
            if (part == null) {
                htmlReport.toHtml(messageForReport, anchorNameForRedirect);
            } else if (THREADS_DUMP_PART.equalsIgnoreCase(part)) {
                httpResponse.setContentType(TEXT_CONTENT_TYPE);
                htmlReport.writeThreadsDump();
            } else {
                doHtmlPart(httpRequest, part, htmlReport);
            }
        } finally {
            writer.close();
        }
    }
