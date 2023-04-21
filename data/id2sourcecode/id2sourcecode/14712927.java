    public void channelUserJoined(ChannelUserEvent e) {
        System.out.println(e.getChannel().getName());
        e.getChannel().sendMessage(HELLO_PHRASES[RANDOM_GENERATOR.nextInt(HELLO_PHRASES.length - 1)] + " " + e.getUser().getNick());
    }
