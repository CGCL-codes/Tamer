    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);
        if (sender.equals(getNick())) {
            System.out.println("Joined channel: " + getChannels()[0]);
            sendMessage(channel, Colors.BOLD + "Hello World !");
            sendMessage(channel, Colors.BOLD + "My name is TrivialBot, i am apparently a trivial bot !");
            sendMessage(channel, Colors.BOLD + "Type !help for a list of available commands");
            final String f = channel;
        }
    }
