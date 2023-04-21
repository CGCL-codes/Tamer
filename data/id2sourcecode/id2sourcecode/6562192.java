    public DataQuery(int type, DN sourceDN, DN newDN, DataBrokerUnthreadedInterface externalDataSource, boolean overwriteExistingData) {
        this(type);
        requestDN = newDN;
        this.oldDN = sourceDN;
        this.externalDataSource = externalDataSource;
        this.overwriteExistingData = overwriteExistingData;
        if (type != DataQuery.XWINCOPY) setException(new Exception("Bad Constructor call (iii) for DataQuery of Type " + type));
    }
