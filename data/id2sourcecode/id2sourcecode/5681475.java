    @Override
    public void channelJoin(Connection source, BNetUser user) {
        writeUserList(source);
        if (GlobalSettings.getDisplayJoinParts()) append(source, user.toString() + " has joined the channel" + user.getStatString().toString() + ".", cs.getChannelColor());
    }
