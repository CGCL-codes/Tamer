    public void publish(Client fromClient, String toChannel, Object data, String msgId) {
        messageLog.add("[" + new Date() + "] channel=" + toChannel + " from=" + fromClient + " data=" + data);
        ChannelImpl channel = (ChannelImpl) getChannel(toChannel, false);
        if (channel == null) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("message received for channel " + toChannel + " which not exists. Ignore message");
            }
            return;
        }
        channel.publish(data, msgId);
    }
