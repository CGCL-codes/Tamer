    HtmlCounterRequestContextReport(List<CounterRequestContext> rootCurrentContexts, Map<String, HtmlCounterReport> counterReportsByCounterName, List<ThreadInformations> threadInformationsList, boolean stackTraceEnabled, int maxContextsDisplayed, Writer writer) {
        super();
        assert rootCurrentContexts != null;
        assert threadInformationsList != null;
        assert writer != null;
        this.rootCurrentContexts = rootCurrentContexts;
        if (counterReportsByCounterName == null) {
            this.counterReportsByCounterName = new HashMap<String, HtmlCounterReport>();
        } else {
            this.counterReportsByCounterName = counterReportsByCounterName;
        }
        this.threadInformationsByThreadId = new HashMap<Long, ThreadInformations>(threadInformationsList.size());
        for (final ThreadInformations threadInformations : threadInformationsList) {
            this.threadInformationsByThreadId.put(threadInformations.getId(), threadInformations);
        }
        this.writer = writer;
        boolean oneRootHasChild = false;
        for (final CounterRequestContext rootCurrentContext : rootCurrentContexts) {
            if (rootCurrentContext.getParentCounter().getChildCounterName() != null) {
                oneRootHasChild = true;
                break;
            }
        }
        this.childHitsDisplayed = oneRootHasChild;
        this.htmlThreadInformationsReport = new HtmlThreadInformationsReport(threadInformationsList, stackTraceEnabled, writer);
        this.stackTraceEnabled = stackTraceEnabled;
        this.maxContextsDisplayed = maxContextsDisplayed;
    }
