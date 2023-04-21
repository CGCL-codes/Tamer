    protected int forEachDataInTable(boolean processBatch, Batch batch, DataContext readerContext, DataContext writerContext) {
        int dataRow = 0;
        Data data = null;
        do {
            data = dataReader.nextData(readerContext);
            if (data != null) {
                try {
                    dataRow++;
                    if (processBatch) {
                        dataWriter.writeData(data, writerContext);
                    }
                } catch (Exception ex) {
                    if (errorHandler != null) {
                        if (!errorHandler.handleWriteError(ex, batch, data, dataRow)) {
                            rethrow(ex);
                        }
                    }
                }
            }
        } while (data != null);
        return dataRow;
    }
