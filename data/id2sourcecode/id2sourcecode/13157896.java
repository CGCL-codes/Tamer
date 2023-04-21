    public boolean updateClientState(ClientState state) {
        if (weJoined(state)) {
            state.addChannel(channel);
            return true;
        } else {
            Channel channelObj = state.getChannel(channel);
            channelObj.addMember(user, this);
            return true;
        }
    }
