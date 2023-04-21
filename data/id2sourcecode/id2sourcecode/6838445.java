    public ChatMsgStorage(ChatChannel theChannel, WebServerOptions options) {
        this.options = options;
        this.channel = theChannel;
        LOGGER.fine("Chat Message Storage instantiated for channel " + getChannel().getChannelId());
        this.lastNChatMessages = new ArrayList<ChatMessage>();
        restoreMessages();
    }
