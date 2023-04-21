    @Override
    protected void handleJoinCompleteEvent(JoinCompleteEvent evt) {
        String greeting = env.getProperty(JerkBotConstants.IRC_GREETING, "");
        if (!StringUtils.isBlank(greeting)) {
            evt.getChannel().say(greeting);
        }
    }
