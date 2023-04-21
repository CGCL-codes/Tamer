    public void publish(Client fromClient, Object data, String msgId) {
        _bayeux.publish(getChannelId(), fromClient, data, msgId);
    }
