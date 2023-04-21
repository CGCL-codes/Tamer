        protected void handleInput(IRCWindow window, String[] tokens) {
            Session session = window.getSession();
            session.invite(tokens[0], session.getChannel(tokens[1]));
        }
