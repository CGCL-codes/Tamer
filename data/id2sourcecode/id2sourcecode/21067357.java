    @Override
    public void whisperSent(Connection source, BNetUser user, String text) {
        lastWhisperTo = user;
        mainTextArea.whisperSent(source.getDisplayType(), user, text);
        if (GlobalSettings.trayDisplayWhisper) notifySystemTray(Growl.CHANNEL_WHISPER_SENT, source.getChannel(), "<To: " + user.toString() + "> " + text);
    }
