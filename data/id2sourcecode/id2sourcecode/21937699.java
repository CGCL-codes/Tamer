    private PircBot startIrcBot() throws UnsupportedEncodingException, IOException, IrcException, NickAlreadyInUseException {
        ircBot = new HipLogBot(config.getNick(), config.getOutputDir(), config.getJoinMessage());
        ircBot.setEncoding(config.getEncoding());
        ircBot.connect(config.getServerName(), config.getServerPort(), config.getServerPassword());
        ircBot.joinChannel(config.getChannel());
        return ircBot;
    }
