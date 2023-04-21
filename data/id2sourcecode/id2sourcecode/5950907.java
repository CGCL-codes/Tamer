    public TransactionResourceControlEvent(Object source, String pdpId, String transactionId, List<String> readlist, List<String> writelistPermit, List<String> writelistDeny, String type) {
        super(source);
        this.pdpId = pdpId;
        this.transactionId = transactionId;
        this.type = type;
        this.readlist = readlist;
        this.writelistPermit = writelistPermit;
        this.writelistDeny = writelistDeny;
    }
