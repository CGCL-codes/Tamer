    public void receivedMessage(ByteBuffer message) {
        GenericEvent event = MessageUtil.decode(message);
        if (event instanceof ChannelEvent) {
            AppContext.getChannelManager().getChannel(CastaneaServer.WORLD).send(sessionRef.get(), MessageUtil.encode(event));
        } else if (event instanceof PlayerEvent) {
            CastaneaPlayer player = (CastaneaPlayer) AppContext.getDataManager().getBinding(event.getRecipient());
            player.send(event);
        } else {
            throw new RuntimeException("Every event received by the server must implement either ChannelEvent or PlayerEvent.");
        }
    }
