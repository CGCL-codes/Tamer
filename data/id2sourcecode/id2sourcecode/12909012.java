    public void onQuit(IRCUser ircUser, String msg) {
        User user = owner.resolveUser(ircUser);
        if (user.getNick().equalsIgnoreCase(owner.getNick())) {
            for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
                Channel channel = (Channel) it.next();
                UserParticipationEvent event = new UserParticipationEvent(owner, channel, user, UserParticipationEvent.QUIT, new Message(owner, msg));
                owner.fireChannelLeft(event);
                owner.removeChannel(channel);
            }
        } else {
            UserParticipationEvent event = new UserParticipationEvent(owner, null, user, UserParticipationEvent.QUIT, new Message(owner, msg));
            owner.fireUserLeft(event);
            for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
                Channel channel = (Channel) it.next();
                UserParticipationEvent event2 = new UserParticipationEvent(owner, channel, user, UserParticipationEvent.QUIT, new Message(owner, msg));
                if (channel.hasUser(user)) {
                    channel.fireUserLeft(event2);
                    channel.removeUser(user);
                }
            }
        }
    }
