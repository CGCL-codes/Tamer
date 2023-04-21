    @Override
    public void recieveChat(Connection source, BNetUser user, String text) {
        mainTextArea.userChat(source.getDisplayType(), user, text, profile.isOneOfMyUsers(user));
        if (GlobalSettings.trayDisplayChatEmote) notifySystemTray(Growl.CHANNEL_USER_CHAT, source.getChannel(), "<" + user.toString() + "> " + text);
    }
