    @Test
    public void testThreadInformations() throws IOException {
        final StringWriter writer = new StringWriter();
        new HtmlThreadInformationsReport(Collections.<ThreadInformations>emptyList(), true, writer).toHtml();
        assertNotEmptyAndClear(writer);
        new HtmlThreadInformationsReport(JavaInformations.buildThreadInformationsList(), true, writer).toHtml();
        assertNotEmptyAndClear(writer);
        new HtmlThreadInformationsReport(JavaInformations.buildThreadInformationsList(), false, writer).toHtml();
        assertNotEmptyAndClear(writer);
        final List<ThreadInformations> threads = new ArrayList<ThreadInformations>();
        final Thread thread = Thread.currentThread();
        final List<StackTraceElement> stackTrace = Arrays.asList(thread.getStackTrace());
        final String hostAddress = Parameters.getHostAddress();
        threads.add(new ThreadInformations(thread, null, 10, 10, false, hostAddress));
        threads.add(new ThreadInformations(thread, Collections.<StackTraceElement>emptyList(), 10, 10, false, hostAddress));
        threads.add(new ThreadInformations(thread, stackTrace, 10, 10, true, hostAddress));
        threads.add(new ThreadInformations(thread, stackTrace, 10, 10, false, hostAddress));
        new HtmlThreadInformationsReport(threads, true, writer).toHtml();
        assertNotEmptyAndClear(writer);
        new HtmlThreadInformationsReport(threads, true, writer).writeDeadlocks();
        assertNotEmptyAndClear(writer);
    }
