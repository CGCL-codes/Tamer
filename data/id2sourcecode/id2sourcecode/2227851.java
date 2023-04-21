    public void speak(String channel, String str) {
        Channel aChannel = session.getChannel(channel);
        if (aChannel == null) {
            throw new RuntimeOperationsException(new NullPointerException(String.format("No such channel '%s'", channel)));
        }
        aChannel.say(str);
    }
