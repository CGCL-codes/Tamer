    @Test
    public void testRootContexts() throws IOException {
        HtmlReport htmlReport;
        counter.addRequest("first request", 100, 100, false, 1000);
        TestCounter.bindRootContexts("first request", counter, 3);
        sqlCounter.bindContext("sql", "sql", null, -1);
        htmlReport = new HtmlReport(collector, null, javaInformationsList, Period.TOUT, writer);
        htmlReport.toHtml("message a", null);
        assertNotEmptyAndClear(writer);
        final Counter myCounter = new Counter("http", null);
        final Collector collector2 = new Collector("test 2", Arrays.asList(myCounter));
        myCounter.bindContext("my context", "my context", null, -1);
        htmlReport = new HtmlReport(collector2, null, javaInformationsList, Period.SEMAINE, writer);
        htmlReport.toHtml("message b", null);
        assertNotEmptyAndClear(writer);
        final HtmlCounterRequestContextReport htmlCounterRequestContextReport = new HtmlCounterRequestContextReport(collector2.getRootCurrentContexts(), null, new ArrayList<ThreadInformations>(), false, 500, writer);
        htmlCounterRequestContextReport.toHtml();
        assertNotEmptyAndClear(writer);
    }
