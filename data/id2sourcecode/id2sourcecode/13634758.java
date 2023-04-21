    public User[] getUsers(String channel, IRCServer server) {
        Channel chanid = getChannelByName(channel.charAt(0), channel.substring(1), server);
        if (chanid != null) {
            return getChannelUserWireCollection().getUserArrayInChannel(chanid);
        } else return null;
    }
