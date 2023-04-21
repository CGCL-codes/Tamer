    public User resolveUser(String nick) {
        if (nick == null) {
            throw new IllegalArgumentException();
        }
        for (final Object o : getChannels()) {
            Channel channel = (Channel) o;
            ChannelUser user = channel.getUser(nick);
            if (user != null) {
                return user.getUser();
            }
        }
        return new User(nick);
    }
