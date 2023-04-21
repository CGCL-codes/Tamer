    protected int forEachTableInBatch(boolean processBatch, Batch batch, DataContext readerContext, DataContext writerContext) {
        int dataRow = 0;
        Table table = null;
        do {
            table = dataReader.nextTable(readerContext);
            if (table != null) {
                readerContext.setSourceTable(table);
                if (processBatch) {
                    writerContext.setSourceTable(table);
                    dataWriter.switchTables(writerContext);
                }
                dataRow += forEachDataInTable(processBatch, batch, readerContext, writerContext);
            }
        } while (table != null);
        return dataRow;
    }
