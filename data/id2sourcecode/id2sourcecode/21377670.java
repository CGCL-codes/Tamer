    protected List<IncomingBatch> loadDataFromTransport(final String sourceNodeId, IIncomingTransport transport) throws IOException {
        final ManageIncomingBatchListener listener = new ManageIncomingBatchListener();
        try {
            long totalNetworkMillis = System.currentTimeMillis();
            if (parameterService.is(ParameterConstants.STREAM_TO_FILE_ENABLED)) {
                IDataReader dataReader = new ProtocolDataReader(transport.open());
                IDataWriter dataWriter = new StagingDataWriter(sourceNodeId, Constants.STAGING_CATEGORY_INCOMING, stagingManager, new LoadIntoDatabaseOnArrivalListener(sourceNodeId, listener));
                new DataProcessor(dataReader, dataWriter).process();
                totalNetworkMillis = System.currentTimeMillis() - totalNetworkMillis;
            } else {
                DataProcessor processor = new DataProcessor(new ProtocolDataReader(transport.open()), null, listener) {

                    @Override
                    protected IDataWriter chooseDataWriter(Batch batch) {
                        return buildDataWriter(sourceNodeId, batch.getChannelId(), batch.getBatchId());
                    }
                };
                processor.process();
            }
            List<IncomingBatch> batchesProcessed = listener.getBatchesProcessed();
            for (IncomingBatch incomingBatch : batchesProcessed) {
                if (incomingBatch.getBatchId() != BatchInfo.VIRTUAL_BATCH_FOR_REGISTRATION && incomingBatchService.updateIncomingBatch(incomingBatch) == 0) {
                    log.error("Failed to update batch {}.  Zero rows returned.", incomingBatch.getBatchId());
                }
            }
        } catch (Exception ex) {
            logAndRethrow(ex);
        } finally {
            transport.close();
        }
        return listener.getBatchesProcessed();
    }
