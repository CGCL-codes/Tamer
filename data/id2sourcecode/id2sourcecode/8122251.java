        protected void handleInput(IRCWindow window, String[] tokens) {
            Session session = window.getSession();
            Channel channel = window.getChannel();
            session.partChannel(channel, tokens[0] == null ? "" : tokens[0]);
            session.joinChannel(channel.getName());
        }
