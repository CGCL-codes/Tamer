    public ChatController(final String name, final Messengers messenger) {
        this(name, messenger.getMessenger(), messenger.getRemoteMessenger(), messenger.getChannelMessenger());
    }
