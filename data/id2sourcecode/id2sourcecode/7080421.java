    @Test
    public void testHtmlCounterRequestContext() throws IOException {
        assertNotNull("HtmlCounterRequestContextReport", new HtmlCounterRequestContextReport(Collections.<CounterRequestContext>emptyList(), null, Collections.<ThreadInformations>emptyList(), true, 500, writer));
        final HtmlCounterRequestContextReport report = new HtmlCounterRequestContextReport(Collections.<CounterRequestContext>emptyList(), Collections.<String, HtmlCounterReport>emptyMap(), Collections.<ThreadInformations>emptyList(), true, 500, writer);
        report.toHtml();
        assertNotEmptyAndClear(writer);
        final List<CounterRequestContext> counterRequestContexts = Collections.singletonList(new CounterRequestContext(sqlCounter, null, "Test", "Test", null, -1));
        final HtmlCounterRequestContextReport report2 = new HtmlCounterRequestContextReport(counterRequestContexts, null, Collections.<ThreadInformations>emptyList(), true, 0, writer);
        report2.toHtml();
        assertNotEmptyAndClear(writer);
        report2.writeTitleAndDetails();
    }
