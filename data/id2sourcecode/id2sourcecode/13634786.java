    public void receivedUserList(Channel channel) {
        super.receivedUserList(channel);
        String chanid = new Character(channel.getScope()).toString() + channel.getName();
        User[] userlist = getChannelUserWireCollection().getUserArrayInChannel(channel);
        onUserList(chanid, userlist, channel.getServer());
    }
