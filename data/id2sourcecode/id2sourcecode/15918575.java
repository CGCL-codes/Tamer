    public CasPrintAlignedReads(CasDataStoreFactory casDataStoreFactory, CasIdLookup contigNameLookup, CasIdLookup readNameLookup, PrintWriter writer) {
        this.casDataStoreFactory = casDataStoreFactory;
        this.writer = writer;
        this.contigNameLookup = contigNameLookup;
        this.readNameLookup = readNameLookup;
    }
