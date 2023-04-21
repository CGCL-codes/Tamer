    private void sendNewChannelNotification(String newchannel) {
        Channel global = AppContext.getChannelManager().getChannel(ChatCommands.GLOBAL_CHANNEL_NAME);
        MsgChat newChannelmsg = new MsgChat(ChatCommands.NEW_CHANNEL, "", newchannel);
        global.send(null, newChannelmsg.toByteBuffer());
    }
