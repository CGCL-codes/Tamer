    HtmlThreadInformationsReport(List<ThreadInformations> threadInformationsList, boolean stackTraceEnabled, Writer writer) {
        super();
        assert threadInformationsList != null;
        assert writer != null;
        this.threadInformationsList = threadInformationsList;
        this.writer = writer;
        this.stackTraceEnabled = stackTraceEnabled;
        this.cpuTimeEnabled = !threadInformationsList.isEmpty() && threadInformationsList.get(0).getCpuTimeMillis() != -1;
    }
