    public void action(String channel, String message) {
        Channel aChannel = session.getChannel(channel);
        if (aChannel == null) {
            throw new RuntimeOperationsException(new NullPointerException(String.format("No such channel '%s'", channel)));
        }
        aChannel.action(message);
    }
