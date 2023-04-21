    public void startBatch(Batch batch) {
        this.batch = batch;
        if (flushNodeId) {
            String sourceNodeId = context.getSourceNodeId();
            if (StringUtils.isNotBlank(sourceNodeId)) {
                println(CsvConstants.NODEID, sourceNodeId);
            }
            BinaryEncoding binaryEncoding = context.getBinaryEncoding();
            if (binaryEncoding != null) {
                println(CsvConstants.BINARY, binaryEncoding.name());
            }
            flushNodeId = false;
        }
        if (StringUtils.isNotBlank(batch.getChannelId())) {
            println(CsvConstants.CHANNEL, batch.getChannelId());
        }
        println(CsvConstants.BATCH, Long.toString(batch.getBatchId()));
    }
