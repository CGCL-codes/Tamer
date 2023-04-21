    public boolean fitsBoardAndChannel(int boardChannel, Board aBoard) {
        return (boardChannel == subchannel && aBoard.getAddress() == address && aBoard.getCommChannel().getChannelName().equals(commChannel));
    }
