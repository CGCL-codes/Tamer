    @Override
    public void whisperRecieved(Connection source, BNetUser user, String text) {
        lastWhisperFrom = user;
        mainTextArea.whisperRecieved(source.getDisplayType(), user, text);
        if (GlobalSettings.trayDisplayWhisper) notifySystemTray(Growl.CHANNEL_WHISPER_RECIEVED, source.getChannel(), "<From: " + user.toString() + "> " + text);
    }
