    public boolean updateClientState(ClientState state) {
        if (kickedUs(state)) {
            state.removeChannel(channel);
            return true;
        } else {
            Channel channelObj = state.getChannel(channel);
            channelObj.removeMember(userKicked, this);
            return true;
        }
    }
