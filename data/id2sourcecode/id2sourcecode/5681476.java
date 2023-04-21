    @Override
    public void channelLeave(Connection source, BNetUser user) {
        writeUserList(source);
        if (GlobalSettings.getDisplayJoinParts()) append(source, user.toString() + " has left the channel.", cs.getChannelColor());
    }
