    @Test
    public void testInitialLoadSql() throws Exception {
        ITriggerRouterService triggerRouterService = getTriggerRouterService();
        TriggerRouter triggerRouter = triggerRouterService.getTriggerRouterForTableForCurrentNode(null, null, TEST_TRIGGERS_TABLE, true);
        Table table = getDbDialect().getTable(triggerRouter.getTrigger().getSourceCatalogName(), triggerRouter.getTrigger().getSourceSchemaName(), triggerRouter.getTrigger().getSourceTableName(), true);
        String sql = getDbDialect().createInitialLoadSqlFor(new Node("1", null, "1.0"), triggerRouter, table, triggerRouterService.getNewestTriggerHistoryForTrigger(triggerRouter.getTrigger().getTriggerId()), getConfigurationService().getChannel(triggerRouter.getTrigger().getChannelId()));
        List<String> csvStrings = getJdbcTemplate().queryForList(sql, String.class);
        assertTrue(csvStrings.size() > 0);
        String csvString = csvStrings.get(0);
        csvString = csvString.replaceFirst("\"00001\\.\"", "\"1\"");
        csvString = csvString.replaceFirst("\"1.0000000000000000\"", "\"1\"");
        assertTrue(csvString.endsWith(EXPECTED_INSERT1_CSV_ENDSWITH), "Received " + csvString + ", Expected the string to end with " + EXPECTED_INSERT1_CSV_ENDSWITH);
    }
