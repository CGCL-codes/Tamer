    public OMSRights(OMSElement grantedFor, String grantedToId, boolean read, boolean write, boolean compose, boolean associate, boolean delete) {
        super(OMSStructure.generateUniqueString(), null, null, null, null, null);
        this.grantedToId = grantedToId;
        this.read = read;
        this.write = write;
        this.compose = compose;
        this.associate = associate;
        this.delete = delete;
        this.grantedFor = grantedFor;
    }
