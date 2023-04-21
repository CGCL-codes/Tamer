    public void run() {
        Channel channel = AppContext.getChannelManager().getChannel(channelName);
        if (!channel.hasSessions()) {
            sendEmptyChannelNotification(channelName);
            AppContext.getDataManager().removeObject(channel);
            PChat.getINSTANCE().removeChannelfromlist(channelName);
        }
    }
