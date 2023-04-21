    public void write(Object data, IStoreType type, java.io.Reader reader, float boost) throws ISFException {
        this.writeData(data, type);
        int fid = this.target.getID();
        this.searchMgr.getIndexManager().index(fid, reader, this.port, this.getIPI(), boost);
    }
