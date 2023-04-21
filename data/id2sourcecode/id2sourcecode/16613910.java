    public DCGParam(Board board, TYPE type) {
        super();
        this.commChannelName = board.getCommChannel().getChannelName();
        this.address = board.getAddress();
        this.type = type;
    }
