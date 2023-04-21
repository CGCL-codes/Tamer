        protected void handleInput(IRCWindow window, String[] tokens) {
            Session selectedSession = window.getSession();
            Channel channel = selectedSession.getChannel(tokens[0]);
            if (channel != null) {
                channel.say(tokens[1]);
            } else {
                selectedSession.sayPrivate(tokens[0], tokens[1]);
            }
            window.insertDefault("[msg >>> " + tokens[0] + "]: " + tokens[1]);
        }
