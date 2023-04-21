    public static void joinChannel(String channelName) {
        if (TabOrganizer.getChannelPanel(channelName) == null) {
            new Message(ClientProtocolCommands.JOIN_CHANNEL, GameDatabase.getIDofGame(channelName));
        } else {
            TabOrganizer.openChannelPanel(channelName);
        }
    }
