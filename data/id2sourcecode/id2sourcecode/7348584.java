    public boolean fitsBoard(Board aBoard) {
        return (aBoard.getAddress() == address && aBoard.getCommChannel().getChannelName().equals(commChannel));
    }
