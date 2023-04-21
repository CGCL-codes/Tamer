    public void processEvent(SessionEvent se) {
        if (se.getType() == SessionEvent.TERMINATE) {
            User u = se.getSession().getUserState().getUser();
            if (u != null) {
                String nickname = u.getNickname();
                Channel[] channels = getChannelsForUser(nickname);
                if (channels != null) {
                    for (int x = 0; x < channels.length; x++) {
                        String channelName = channels[x].getName();
                        removeUserFromChannel(channelName, nickname);
                        notifyChannelOfUserAction(ACTION_LEAVE, channelName, nickname);
                    }
                }
            }
        }
    }
