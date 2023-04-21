    @Override
    public void recieveEmote(Connection source, BNetUser user, String text) {
        mainTextArea.userEmote(source.getDisplayType(), user, text);
        if (GlobalSettings.trayDisplayChatEmote) notifySystemTray(Growl.CHANNEL_USER_EMOTE, source.getChannel(), "<" + user.toString() + " " + text + ">");
    }
