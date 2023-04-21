    public void process() {
        DataContext readerContext = dataReader.createDataContext();
        DataContext writerContext = dataWriter.createDataContext();
        dataReader.open(readerContext);
        Batch batch = null;
        do {
            batch = dataReader.nextBatch(readerContext);
            if (batch != null) {
                int dataRow = 0;
                readerContext.setBatch(batch);
                boolean processBatch = listener.batchBegin(batch);
                if (processBatch) {
                    writerContext.setBatch(batch);
                    dataWriter.startBatch(writerContext);
                }
                dataRow += forEachTableInBatch(processBatch, batch, readerContext, writerContext);
                if (processBatch) {
                    listener.batchBeforeCommit(batch);
                    dataWriter.finishBatch(writerContext);
                    listener.batchCommit(batch);
                }
            }
        } while (batch != null);
    }
