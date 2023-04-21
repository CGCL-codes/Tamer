    public ExceptionWriter(Node pXMLConfig, int pPartitionID, int pPartition, ETLThreadManager pThreadManager) throws KETLThreadException {
        super(pXMLConfig, pPartitionID, pPartition, pThreadManager);
        if (pPartition > 1) {
            throw new KETLThreadException("Exception writer cannot be run in parallel, or multiple exceptions will be generated, please set FLOWTYPE=\"FANIN\". Requested partitions: " + pPartition, this);
        }
    }
