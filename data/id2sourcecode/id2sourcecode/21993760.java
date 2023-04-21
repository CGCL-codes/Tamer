    public Channel getChannelForName(String name) {
        Channel result = null;
        for (Channel c : _channels) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return result;
    }
