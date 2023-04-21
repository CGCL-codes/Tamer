    private void channelMsgReceived(String channel, String msg) {
        ChannelManager channelMgr = AppContext.getChannelManager();
        Channel ch = channelMgr.getChannel(channel);
        MsgChat message = new MsgChat();
        message.setCommand(ChatCommands.CHANNEL_MSG);
        message.setMsg(channel + " " + msg);
        message.setSender(this.getPlayerAssociated().getSession().getName());
        ch.send(null, message.toByteBuffer());
    }
