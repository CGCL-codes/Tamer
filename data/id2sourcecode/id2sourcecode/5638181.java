    void setSourceQueue(ManagedBlockingQueue srcQueue, ETLWorker worker) throws KETLThreadException {
        this.getUsedPortsFromWorker(worker, ETLWorker.getChannel(this.getXMLConfig(), ETLWorker.DEFAULT));
        this.srcQueue = srcQueue;
        try {
            this.mExpectedDataTypes = worker.getOutputRecordDatatypes(ETLWorker.getChannel(this.getXMLConfig(), ETLWorker.DEFAULT));
            this.mRecordWidth = this.mExpectedDataTypes.length;
        } catch (ClassNotFoundException e) {
            throw new KETLThreadException(e, this);
        }
        this.configureBufferSort(srcQueue);
    }
