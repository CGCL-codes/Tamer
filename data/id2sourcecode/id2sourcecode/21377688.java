        public void end(Batch batch, IStagedResource resource) {
            if (listener.currentBatch != null) {
                listener.currentBatch.setNetworkMillis(System.currentTimeMillis() - batchStartsToArriveTimeInMs);
            }
            try {
                DataProcessor processor = new DataProcessor(new ProtocolDataReader(resource), null, listener) {

                    @Override
                    protected IDataWriter chooseDataWriter(Batch batch) {
                        return buildDataWriter(sourceNodeId, batch.getChannelId(), batch.getBatchId());
                    }
                };
                processor.process();
            } finally {
                resource.setState(State.DONE);
            }
        }
