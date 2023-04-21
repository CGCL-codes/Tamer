        public void join(String s, User c) throws IOException {
            ((IRCChannel) getRoom(s)).joins(ircController.getChannelRoleByChannelName((IRCUser) c, s));
        }
