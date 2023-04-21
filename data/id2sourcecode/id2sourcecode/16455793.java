    @Test
    public void testCrashRestartNonBatchWriter() throws Throwable {
        outputLogToFile(CrashRestartRunner.class);
        fillTable("test_reader_table", 30000, "NAME", "ADDRESS", "PHONE");
        CrashRestartRunner runner = new CrashRestartRunner();
        runner.setPathToFlow(getTestMethodInputDirectory() + "Database-reader-writer-flow.xml");
        runner.setPathToRuntimeConfiguration(getTestMethodInputDirectory() + "runtime-parameters.properties");
        runner.setPathToRestartRuntimeConfiguration(getTestMethodInputDirectory() + "restart-runtime-parameters.properties");
        runner.setLoggingConfiguration(getTestMethodInputDirectory() + "logging-configuration.properties");
        runner.crashAndRestart(8000, false);
        assertFalse(FileUtils.isDirectoryEmpty(getTestMethodOutputDirectory() + "operators/operator_0/checkpoints"));
        assertFalse(FileUtils.isDirectoryEmpty(getTestMethodOutputDirectory() + "operators/operator_1/checkpoints"));
        assertFalse(FileUtils.isDirectoryEmpty(getTestMethodOutputDirectory() + "operators/operator_2/checkpoints"));
        testStartDerbyServer();
        tableRegressionCheck("test_writer_table", 30000);
        assertBaselines();
    }
