    public boolean updateClientState(ClientState state) {
        if (oldNick.equals(state.getNick())) {
            state.setNick(newNick);
            return true;
        } else {
            Enumeration channels = state.getChannels();
            while (channels.hasMoreElements()) {
                Channel channel = (Channel) channels.nextElement();
                Member member = channel.findMember(oldNick.getNick());
                if (member != null) member.setNick(newNick);
            }
        }
        return false;
    }
