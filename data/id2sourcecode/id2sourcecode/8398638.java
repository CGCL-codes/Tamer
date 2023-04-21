    private void runIntegratedTestAndConfirmOutputResults(String syntaxName) throws Exception {
        StringWriter writer = new StringWriter();
        ChangeScript changeOne = new StubChangeScript(1, "001_change.sql", "-- contents of change script 1");
        ChangeScript changeTwo = new StubChangeScript(2, "002_change.sql", "-- contents of change script 2");
        List<ChangeScript> changeScripts = Arrays.asList(changeOne, changeTwo);
        ChangeScriptRepository changeScriptRepository = new ChangeScriptRepository(changeScripts);
        final StubSchemaManager schemaManager = new StubSchemaManager();
        ChangeScriptApplier applier = new TemplateBasedApplier(writer, syntaxName, "changelog", ";", DelimiterType.normal, null);
        Controller controller = new Controller(changeScriptRepository, schemaManager, applier, null);
        controller.processChangeScripts(Long.MAX_VALUE);
        assertEquals(readExpectedFileContents(getExpectedFilename(syntaxName)), writer.toString());
    }
