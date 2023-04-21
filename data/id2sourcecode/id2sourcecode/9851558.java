    public void setupRunners() throws Exception {
        logger.info(formatMessage("SETTING UP TABLES"));
        CycleRunnerUtils setup = new CycleRunnerUtils(writerConfig, CycleRunnerUtils.CREATE, 0, true);
        (new Thread(setup, setup.getClass().getSimpleName())).start();
        logger.info(formatMessage("WAITING FOR SETUP COMPLETION"));
        ExecutionResult setupResult = setup.get();
        assertTrue("SETUP FAILED:" + setupResult, setupResult.getStatus() == ExecutionStatus.SUCCEEDED);
        doneLatch = new CountDownLatch(smokeConfig.getThreads() * 2);
        readers = new Vector<CycleRunner>();
        writers = new Vector<CycleRunner>();
        for (int i = 0; i < smokeConfig.getThreads(); i++) {
            CycleRunner writer = new CycleRunner(i, smokeConfig.getRowCount(), smokeConfig.getRunTime(), writerConfig, null, startLatch, doneLatch, setup);
            writers.add(writer);
            CycleRunner reader = new CycleRunner(i, smokeConfig.getRowCount(), smokeConfig.getRunTime(), readerConfig, writer, startLatch, doneLatch, setup);
            readers.add(reader);
            writer.setCompanion(reader);
            (new Thread(writer, writer.getClass().getSimpleName() + "-Writer-" + i)).start();
            (new Thread(reader, reader.getClass().getSimpleName() + "-Reader-" + i)).start();
        }
    }
