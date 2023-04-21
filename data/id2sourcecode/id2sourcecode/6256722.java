    public OMSRights cloneRights() {
        return new OMSRights(this.grantedFor, this.grantedToId, this.read, this.write, this.compose, this.associate, this.delete);
    }
