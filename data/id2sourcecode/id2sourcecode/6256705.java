    public OMSRights(OMSElement grantedFor, String grantedToId, boolean read, boolean write, boolean compose, boolean associate, boolean delete, String id, Date creatingDate, String creater, Date modifyingDate, String modifier) {
        super(id, null, creatingDate, creater, modifyingDate, modifier);
        this.grantedFor = grantedFor;
        this.grantedToId = grantedToId;
        this.read = read;
        this.write = write;
        this.compose = compose;
        this.associate = associate;
        this.delete = delete;
    }
