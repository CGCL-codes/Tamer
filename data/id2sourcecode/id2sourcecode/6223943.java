    @Override
    public void actionPerformed(MessageQueueEvent e) {
        if (e.getMsgtype() == MessageQueueEnum.MSG_PRIVATE_OUT) {
            PrivateMessageResponse pmr = (PrivateMessageResponse) e.getData();
            SendMessage(pmr.getDestination().getNick(), TemplateManager.replace(pmr.getMessage()));
        }
        if (e.getMsgtype() == MessageQueueEnum.CHANNEL_USR_QUIT) {
            getIrcclient().partChannel((Channel) e.getData());
        }
        if (e.getMsgtype() == MessageQueueEnum.CHANNEL_USR_JOIN) {
            String channel = e.getData().toString();
            addDebug(Level.INFO, "Joining channel(s) %s", channel);
            if (channel.contains(",")) {
                String[] channels = channel.split(",");
                for (int i = 0; i < channels.length; i++) {
                    getIrcclient().joinChannel(channels[i]);
                }
            } else {
                getIrcclient().joinChannel(channel);
            }
        }
        if (e.getMsgtype() == MessageQueueEnum.MSG_CHANNEL_OUT) {
            ChannelMessageResponse mug = (ChannelMessageResponse) e.getData();
            getIrcclient().sendMessage(mug.getChannel().getName(), TemplateManager.replace(mug.getMessage()));
        }
        super.actionPerformed(e);
    }
