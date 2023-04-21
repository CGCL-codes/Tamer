    private void sendMessage(String message) {
        ircBot.sendMessage(config.getChannel(), message);
        ircBot.append(LogBot.BLACK, "<" + config.getNick() + "> " + message);
    }
