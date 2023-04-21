    @Test
    public void testOtherWrites() throws Exception {
        final HtmlReport htmlReport = new HtmlReport(collector, null, javaInformationsList, Period.SEMAINE, writer);
        htmlReport.writeAllCurrentRequestsAsPart(true);
        assertNotEmptyAndClear(writer);
        htmlReport.writeAllCurrentRequestsAsPart(false);
        assertNotEmptyAndClear(writer);
        htmlReport.writeAllThreadsAsPart();
        assertNotEmptyAndClear(writer);
        htmlReport.writeSessionDetail("", null);
        assertNotEmptyAndClear(writer);
        htmlReport.writeSessions(Collections.<SessionInformations>emptyList(), "message", SESSIONS_PART);
        assertNotEmptyAndClear(writer);
        htmlReport.writeSessions(Collections.<SessionInformations>emptyList(), null, SESSIONS_PART);
        assertNotEmptyAndClear(writer);
        htmlReport.writeMBeans(false);
        assertNotEmptyAndClear(writer);
        htmlReport.writeMBeans(true);
        assertNotEmptyAndClear(writer);
        htmlReport.writeProcesses(ProcessInformations.buildProcessInformations(getClass().getResourceAsStream("/tasklist.txt"), true));
        assertNotEmptyAndClear(writer);
        htmlReport.writeProcesses(ProcessInformations.buildProcessInformations(getClass().getResourceAsStream("/ps.txt"), false));
        assertNotEmptyAndClear(writer);
        HtmlReport.writeAddAndRemoveApplicationLinks(null, writer);
        assertNotEmptyAndClear(writer);
        HtmlReport.writeAddAndRemoveApplicationLinks("test", writer);
        assertNotEmptyAndClear(writer);
        final Connection connection = TestDatabaseInformations.initH2();
        try {
            htmlReport.writeDatabase(new DatabaseInformations(0));
            assertNotEmptyAndClear(writer);
            htmlReport.writeDatabase(new DatabaseInformations(3));
            assertNotEmptyAndClear(writer);
            JavaInformations.setWebXmlExistsAndPomXmlExists(true, true);
            htmlReport.toHtml(null, null);
            assertNotEmptyAndClear(writer);
            setProperty(Parameter.SYSTEM_ACTIONS_ENABLED, Boolean.TRUE.toString());
            htmlReport.toHtml(null, null);
            assertNotEmptyAndClear(writer);
            setProperty(Parameter.NO_DATABASE, Boolean.TRUE.toString());
            htmlReport.toHtml(null, null);
            assertNotEmptyAndClear(writer);
        } finally {
            connection.close();
        }
    }
