    public void copyFromFiles(File[] sourceFiles) {
        for (File file : sourceFiles) {
            if (file.exists()) {
                long ts = System.currentTimeMillis();
                DataProcessor processor = new DataProcessor(new CsvDataReader(file), getDataWriter(false, file.length()));
                LoadListenerListener loadListener = new LoadListenerListener();
                processor.setListener(loadListener);
                processor.process(new DataContext(parameters));
                long totalTableCopyTime = System.currentTimeMillis() - ts;
                if (loadListener.getTable() != null) {
                    Batch batch = loadListener.getBatch();
                    logger.info("It took %d ms to copy %d rows from table %s.  It took %d ms to read the data and %d ms to write the data. %d rows were inserted.", totalTableCopyTime, batch.getLineCount(), loadListener.getTable().getTableName(), batch.getDataReadMillis(), batch.getDataWriteMillis(), batch.getInsertCount());
                    if (batch.getFallbackUpdateCount() > 0) {
                        logger.info("The data loader fell back to an update %d times during the load", batch.getFallbackUpdateCount());
                    }
                    if (batch.getInsertCollisionCount() > 0) {
                        logger.info("The data loader collided %d times during the load.  All row collisions were ignored.", batch.getInsertCollisionCount());
                    }
                }
            } else {
                logger.error("Could not find " + file.getName());
            }
        }
    }
