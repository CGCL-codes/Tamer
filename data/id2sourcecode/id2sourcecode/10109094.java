    @Override
    public final void leftChannel(final ClientChannel channel) {
        GameContext.getClientCommunication().getChannelConteiner().removeChannel(channel);
    }
