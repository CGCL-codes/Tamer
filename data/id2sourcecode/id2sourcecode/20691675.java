    public void copyFromTables(List<TableToExtract> tables) {
        long batchId = 1;
        for (TableToExtract tableToRead : tables) {
            logger.info("(%d of %d) Copying table %s ", batchId, tables.size(), tableToRead.getTable().getTableName());
            Batch batch = new Batch(batchId++);
            int expectedCount = this.sourceDbDialect.getSqlTemplate().queryForInt(this.sourceDbDialect.getDataCaptureBuilder().createTableExtractCountSql(tableToRead, parameters));
            long ts = System.currentTimeMillis();
            DataProcessor processor = new DataProcessor(new SqlTableDataReader(this.sourceDbDialect, batch, tableToRead), getDataWriter(true, expectedCount));
            processor.process(new DataContext(parameters));
            long totalTableCopyTime = System.currentTimeMillis() - ts;
            logger.info("It took %d ms to copy %d rows from table %s.  It took %d ms to read the data and %d ms to write the data.", totalTableCopyTime, batch.getLineCount(), tableToRead.getTable().getTableName(), batch.getDataReadMillis(), batch.getDataWriteMillis());
        }
    }
