    private void sendEmptyChannelNotification(String empty) {
        Channel global = AppContext.getChannelManager().getChannel(ChatCommands.GLOBAL_CHANNEL_NAME);
        MsgChat newChannelmsg = new MsgChat(ChatCommands.EMPTY_CHANNEL, "", empty);
        global.send(null, newChannelmsg.toByteBuffer());
    }
