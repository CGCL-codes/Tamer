    @Override
    public void channelUser(Connection source, BNetUser user) {
        writeUserList(source);
        if (GlobalSettings.displayChannelUsers) append(source, user + user.getStatString().toString() + ".", cs.getChannelColor());
    }
