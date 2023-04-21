    private void removeFromChannel(String channelName) {
        ChannelManager channelMgr = AppContext.getChannelManager();
        Channel channel;
        MsgChat m = new MsgChat();
        try {
            channel = channelMgr.getChannel(channelName);
        } catch (NameNotBoundException e) {
            return;
        }
        if (this.getPlayerAssociated().getSession().isConnected()) {
            channel.leave(this.getPlayerAssociated().getSession());
        }
        m.setCommand(ChatCommands.LEFT);
        m.setSender(channel.getName());
        m.setMsg(this.getPlayerAssociated().getSession().getName());
        channel.send(null, m.toByteBuffer());
        AppContext.getTaskManager().scheduleTask(new CleanupChannelTask(m, channelName));
    }
