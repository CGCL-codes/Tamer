    @Override
    public void channelJoin(Connection source, BNetUser user) {
        userList.showUser(source, user);
        if (GlobalSettings.getDisplayJoinParts()) mainTextArea.channelInfo(user.toStringEx() + " has joined the channel" + user.getStatString().toString() + ".");
        if (GlobalSettings.trayDisplayJoinPart) notifySystemTray(Growl.CHANNEL_USER_JOIN, source.getChannel(), user.toString() + " joined");
        channelTextPane.setText(channel + " (" + userList.count() + ")");
    }
