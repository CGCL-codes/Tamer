    public void kick(String channel, String user, String reason) {
        Channel aChannel = session.getChannel(channel);
        if (aChannel == null) {
            throw new RuntimeOperationsException(new NullPointerException(String.format("No such channel '%s'", channel)));
        }
        aChannel.kick(user, reason);
    }
