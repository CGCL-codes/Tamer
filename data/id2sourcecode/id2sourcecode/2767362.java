    public void subscribe(String toChannel, Client subscriber) {
        Channel channel = getChannel(toChannel, true);
        channel.subscribe(subscriber);
    }
