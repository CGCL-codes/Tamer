    @Override
    public void receive(Event event) throws EventNotSupportedException {
        if (event instanceof GameBoardEvent) {
            GameBoardEvent gameBoardEvent = (GameBoardEvent) event;
            ServerChannel channel = bayeux.getChannel("/service/gameBoard");
            Mutable message = bayeux.newMessage();
            message.setData(gameBoardEvent.getBoard());
            channel.publish(this.serverSession, message);
        } else {
            logger.warn("Ignoring event");
            throw new EventNotSupportedException(event);
        }
    }
