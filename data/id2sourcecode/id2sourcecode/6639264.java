    public boolean equals(Object other) {
        boolean equal = true;
        if (!(other instanceof Board)) return false;
        Board otherBoard = (Board) other;
        boolean channelFits = this.getCommChannel().getChannelName().equals(otherBoard.getCommChannel().getChannelName());
        boolean addressFits = this.getAddress() == otherBoard.getAddress();
        boolean typeFits = this.getBoardIdentifier().equals(otherBoard.getBoardIdentifier());
        return channelFits && addressFits && typeFits;
    }
