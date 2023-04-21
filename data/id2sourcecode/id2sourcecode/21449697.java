    @Override
    public void addChannel(String channel) {
        addChannel(new BotChannel(channel, bot));
        bot.joinedChannel(getChannel(channel));
    }
