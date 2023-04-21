    public ClientMqMsgListener(ClientProps clientProps) {
        onMessageEvt = clientProps.isPooling() ? new OnMessageP() : new OnMessageNp();
        clientChannelPool = Context.getInstance().getChannelManager().createClientChannelPool(clientProps, onMessageEvt);
        queue = new GmQueue<InternalMessage>(clientProps.getMqMsgQueueCacheSize());
    }
