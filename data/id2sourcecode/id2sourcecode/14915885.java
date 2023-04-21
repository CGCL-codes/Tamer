    public Channel getChannel(int number) {
        if (!channels.containsKey(Integer.valueOf(number))) {
            throw new NullPointerException("There is no such channel");
        }
        return channels.get(Integer.valueOf(number));
    }
