    public void receiveEvent(IRCEvent ircEvent) {
        if (ircEvent.getType() == IRCEvent.Type.CHANNEL_MESSAGE) {
            MessageEvent evt = (MessageEvent) ircEvent;
            if (!evt.isPrivate()) {
                if (channelList.isEmpty()) {
                    return;
                }
                final String channel = evt.getChannel().getName();
                if (isLogged(channel)) {
                    Session session = ircEvent.getSession();
                    Channel _channel = session.getChannel(channel);
                    if (_channel == null) {
                        removeLoggedChannel(channel);
                        return;
                    }
                    final String message = evt.getMessage();
                    final String sender = evt.getNick();
                    IRCLog ircLog = new IRCLog(channel, sender, message);
                    Log.info(ircLog);
                }
            }
        }
    }
