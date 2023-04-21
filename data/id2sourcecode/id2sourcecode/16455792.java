    @Test
    public void testCrashRestart() throws Throwable {
        outputLogToFile(CrashRestartRunner.class);
        fillTable("test_reader_table", 30000, "NAME", "ADDRESS", "PHONE");
        CrashRestartRunner runner = new CrashRestartRunner();
        runner.setPathToFlow(getTestClassInputDirectory() + "Database-reader-writer-flow.xml");
        runner.applyDefaultConfiguration(getTestMethodInputDirectory());
        runner.crashAndRestart(8000, false);
        assertFalse(FileUtils.isDirectoryEmpty(getTestMethodOutputDirectory() + "operators/operator_0/checkpoints"));
        assertFalse(FileUtils.isDirectoryEmpty(getTestMethodOutputDirectory() + "operators/operator_1/checkpoints"));
        assertFalse(FileUtils.isDirectoryEmpty(getTestMethodOutputDirectory() + "operators/operator_2/checkpoints"));
        testStartDerbyServer();
        tableRegressionCheck("test_writer_table", 30000);
        assertBaselines();
    }
