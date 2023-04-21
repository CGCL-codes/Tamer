    public void unsubscribe(String toChannel, Client subscriber) {
        Channel channel = getChannel(toChannel, false);
        if (channel != null) {
            channel.unsubscribe(subscriber);
        }
    }
