    public Channel getChannel(String value) {
        for (Channel c : channels) {
            if (c.name.equals(value)) {
                return c;
            }
        }
        return null;
    }
