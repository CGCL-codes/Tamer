    private String getThreadsDump() throws IOException {
        final StringWriter writer = new StringWriter();
        final HtmlThreadInformationsReport htmlThreadInformationsReport = new HtmlThreadInformationsReport(threadInformationsList, true, writer);
        htmlThreadInformationsReport.writeThreadsDump();
        return writer.toString();
    }
