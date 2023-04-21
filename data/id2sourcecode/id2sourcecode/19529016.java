    public boolean updateClientState(ClientState state) {
        Channel chan = state.getChannel(channel);
        chan.setTopic(topic);
        chan.setTopicDate(new Date());
        return true;
    }
