    public String getBoardInstanceIdentifier() {
        return this.commChannel.getChannelName() + "_" + this.getBoardIdentifier() + "(" + this.getAddress() + ")";
    }
