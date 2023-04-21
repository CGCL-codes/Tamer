    public String getBoardInstanceIdentifierForScripting() {
        return this.commChannel.getChannelName() + "_" + this.getBoardIdentifier() + "_" + this.getAddress();
    }
