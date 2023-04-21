    public boolean updateClientState(ClientState state) {
        Channel channel = state.getChannel(channelName);
        channel.setTopicDate(date);
        channel.setTopicAuthor(author);
        return true;
    }
