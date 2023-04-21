    @Test
    public void testRunWithCheckpoints() throws Throwable {
        outputLogToFile(OhuaLoggerFactory.getLogIDForOperator(DatabaseReaderOperator.class, "DerbyReader", "1") + "-result", Level.INFO);
        fillTable("test_reader_table", 30000, "NAME", "ADDRESS", "PHONE");
        runFlow(getTestClassInputDirectory() + "Database-reader-writer-flow.xml", getTestMethodInputDirectory() + "runtime-parameters.properties");
        CheckpointRegressionAsserts.assertCheckpointsTaken(getTestMethodOutputDirectory(), 4, 3);
        CheckpointRegressionAsserts.assertCheckpointBalance(getTestMethodOutputDirectory(), new int[] { 1 }, new int[] { 3 });
        tableRegressionCheck("test_writer_table", 30000);
    }
