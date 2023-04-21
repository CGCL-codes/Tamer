    private void addToChannel(String channelName) {
        ChannelManager channelMgr = AppContext.getChannelManager();
        Channel channel;
        boolean newChannel = false;
        try {
            channel = channelMgr.getChannel(channelName);
        } catch (NameNotBoundException e) {
            channel = channelMgr.createChannel(channelName, new ar.edu.unicen.exa.server.chat.ChatChannelListener(), Delivery.RELIABLE);
            newChannel = true;
            if (!this.channelsCreated.contains(channelName)) {
                channelsCreated.add(channelName);
            }
            LOGGER.info("New channel created: " + channelName);
        }
        MsgChat changeMsg = new MsgChat(ChatCommands.JOINED, channelName, this.getPlayerAssociated().getSession().getName());
        channel.send(null, changeMsg.toByteBuffer());
        channel.join(this.getPlayerAssociated().getSession());
        if (newChannel) {
            sendNewChannelNotification(channelName);
        }
        sendMembersNotification(channel);
    }
