    public CommunicationException(ChannelHandler handler, Throwable cause) {
        super("Channel " + handler.getEndpoint().getChannel().getName() + " has communication problems on " + Endpoint.getMnemonic(handler.getEndpoint().getSide()) + " side", cause);
    }
