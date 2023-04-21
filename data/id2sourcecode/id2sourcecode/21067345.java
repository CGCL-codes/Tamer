    @Override
    public void channelLeave(Connection source, BNetUser user) {
        userList.removeUser(user);
        if (GlobalSettings.getDisplayJoinParts()) mainTextArea.channelInfo(user.toStringEx() + " has left the channel.");
        if (GlobalSettings.trayDisplayJoinPart) notifySystemTray(Growl.CHANNEL_USER_PART, source.getChannel(), user.toString() + " left");
        channelTextPane.setText(channel + " (" + userList.count() + ")");
    }
