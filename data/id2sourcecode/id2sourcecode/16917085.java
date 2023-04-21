    public char getUserType(Channel channel, User user) {
        channel = getChannel(channel);
        if (channel != null) {
            return channel.getUser(user).getType();
        } else {
        }
        return ' ';
    }
