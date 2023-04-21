    public User resolveUser(IRCUser ircUser) {
        if (ircUser == null) {
            throw new IllegalArgumentException();
        }
        for (final Object o : getChannels()) {
            Channel channel = (Channel) o;
            ChannelUser user = channel.getUser(ircUser.getNick());
            if (user != null) {
                user.update(ircUser);
                return user.getUser();
            }
        }
        return new User(ircUser);
    }
