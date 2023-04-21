        public SelectFromSymDataSource(OutgoingBatch outgoingBatch, Node targetNode) {
            this.batch = new Batch(outgoingBatch.getBatchId(), outgoingBatch.getChannelId(), symmetricDialect.getBinaryEncoding(), outgoingBatch.getNodeId());
            this.targetNode = targetNode;
        }
