        protected void handleInput(IRCWindow window, String[] tokens) {
            Session selectedSession = window.getSession();
            Channel channel = selectedSession.getChannel(tokens[0]);
            if ((tokens[1] != null) && (channel != null)) {
                selectedSession.mode(channel, tokens[1]);
            } else if (channel != null) {
                selectedSession.sayRaw("MODE " + tokens[0]);
            }
        }
