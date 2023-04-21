    public void start(Batch batch) {
        this.statistics.put(batch, new Statistics());
        this.batch = batch;
        if (listeners != null) {
            for (IProtocolDataWriterListener listener : listeners) {
                listener.start(batch);
            }
        }
        if (flushNodeId) {
            if (StringUtils.isNotBlank(sourceNodeId)) {
                println(CsvConstants.NODEID, sourceNodeId);
            }
            BinaryEncoding binaryEncoding = batch.getBinaryEncoding();
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
