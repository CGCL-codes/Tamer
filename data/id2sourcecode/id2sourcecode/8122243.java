        protected void handleInput(IRCWindow window, String[] tokens) {
            String input = (char) 1 + tokens[1] + (char) 1;
            Session selectedSession = window.getSession();
            Channel channel = selectedSession.getChannel(tokens[0]);
            if (channel != null) {
                channel.say(input);
            } else {
                selectedSession.sayPrivate(tokens[0], input);
            }
            window.insertDefault("[ctcp >>> " + tokens[0] + "]: " + tokens[1]);
        }
