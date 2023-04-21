    private void writeContext(CounterRequestContext rootContext, boolean displayRemoteUser) throws IOException {
        final ThreadInformations threadInformations = threadInformationsByThreadId.get(rootContext.getThreadId());
        write("<td valign='top'>");
        final String espace = "&nbsp;";
        if (threadInformations == null) {
            write(espace);
        } else {
            htmlThreadInformationsReport.writeThreadWithStackTrace(threadInformations);
        }
        if (displayRemoteUser) {
            write("</td> <td valign='top'>");
            if (rootContext.getRemoteUser() == null) {
                write(espace);
            } else {
                write(rootContext.getRemoteUser());
            }
        }
        final List<CounterRequestContext> contexts = new ArrayList<CounterRequestContext>(3);
        contexts.add(rootContext);
        contexts.addAll(rootContext.getChildContexts());
        final CounterRequestContextReportHelper counterRequestContextReportHelper = new CounterRequestContextReportHelper(contexts, childHitsDisplayed);
        write("</td> <td>");
        writeRequests(contexts, counterRequestContextReportHelper);
        write("</td> <td align='right' valign='top'>");
        writeDurations(contexts);
        for (final int[] requestValues : counterRequestContextReportHelper.getRequestValues()) {
            writeRequestValues(requestValues);
        }
        if (stackTraceEnabled) {
            write("</td> <td valign='top'>");
            if (threadInformations == null) {
                write(espace);
            } else {
                htmlThreadInformationsReport.writeExecutedMethod(threadInformations);
            }
        }
        write("</td>");
    }
