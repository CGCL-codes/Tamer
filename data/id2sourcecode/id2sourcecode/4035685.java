    @EventSubscriber
    public void onInviteEvent(InviteEvent event) {
        if (_config.getBot().getBotName().equalsIgnoreCase(event.getTargetBot())) {
            ReplacerEnvironment env = new ReplacerEnvironment(SiteBot.GLOBAL_ENV);
            env.add("user", event.getUser().getName());
            env.add("nick", event.getIrcNick());
            if (event.getCommand().equals("INVITE")) {
                outputSimpleEvent(ReplacerUtils.jprintf(_keyPrefix + ".invite.success", env, _bundle), "invite");
                for (ChannelConfig chan : _config.getBot().getConfig().getChannels()) {
                    if (chan.isPermitted(event.getUser())) {
                        _config.getBot().sendInvite(event.getIrcNick(), chan.getName());
                    }
                }
            } else if (event.getCommand().equals("BINVITE")) {
                outputSimpleEvent(ReplacerUtils.jprintf(_keyPrefix + ".invite.failed", env, _bundle), "invite");
            }
        }
    }
