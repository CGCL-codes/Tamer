    public SnmpClient(String readCommunity, String writeCommunity, int timeout, int retries) {
        super();
        this.readCommunity = readCommunity;
        this.writeCommunity = writeCommunity;
        this.timeout = timeout;
        this.retries = retries;
    }
