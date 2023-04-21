    public Channel getChannel(String channel) {
        if (!list.containsKey(channel)) {
            return null;
        }
        return (Channel) list.get(channel);
    }
