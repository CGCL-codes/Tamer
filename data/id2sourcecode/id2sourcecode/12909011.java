    public void onNick(IRCUser ircUser, String newNick) {
        User user = owner.resolveUser(ircUser);
        String oldNick = user.getNick();
        user.setNick(newNick);
        NickEvent event = new NickEvent(owner, user, oldNick);
        for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
            Channel channel = (Channel) it.next();
            if (channel.hasUser(oldNick)) {
                ChannelUser channelUser = channel.removeUser(oldNick);
                channel.addUser(channelUser);
            }
        }
        owner.fireNickChanged(event);
        for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
            Channel channel = (Channel) it.next();
            if (channel.hasUser(user)) {
                channel.fireNickChanged(event);
            }
        }
    }
