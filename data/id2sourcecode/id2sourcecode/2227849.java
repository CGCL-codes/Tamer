    public void deop(String channel, String nick) {
        Channel aChannel = session.getChannel(channel);
        if (aChannel == null) {
            throw new RuntimeOperationsException(new NullPointerException(String.format("No such channel '%s'", channel)));
        }
        aChannel.deop(nick);
    }
