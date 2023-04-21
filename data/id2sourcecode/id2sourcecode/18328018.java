    public boolean updateClientState(ClientState state) {
        Channel channel = state.getChannel(channelName);
        channel.setCreationDate(date);
        return true;
    }
