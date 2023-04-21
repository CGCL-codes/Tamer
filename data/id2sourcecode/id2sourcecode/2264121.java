    public void sendMessage(ChatClient chatClient, Message message) {
        ClientChannelHandler cch = chatClient.getChannelHandler();
        cch.sendPacket(new SM_CHANNEL_MESSAGE(message));
    }
