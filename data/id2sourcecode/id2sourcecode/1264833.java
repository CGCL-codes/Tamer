    public ExcelWriter(Node pXMLConfig, int pPartitionID, int pPartition, ETLThreadManager pThreadManager) throws KETLThreadException {
        super(pXMLConfig, pPartitionID, pPartition, pThreadManager);
        if (pPartition > 1) throw new KETLThreadException("Excel writer cannot be executed in parallel, add FLOWTYPE=\"FANIN\" to step definition", this);
    }
