    public <V> Channel<V> getChannel(String name) {
        Channel<V> channel = channelMap.get(name);
        if (channel == null) {
            channel = new Channel<V>();
            channelMap.put(name, channel);
        }
        return channel;
    }
