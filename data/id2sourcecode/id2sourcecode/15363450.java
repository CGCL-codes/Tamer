        public void kick(String s, String s1, String s2, String s3) throws IOException {
            ((IRCChannel) getRoom(s1)).kicked(ircController.getChannelRoleByChannelName(ircController.getModifyCreateUser(s, null, null), s1), ircController.getModifyCreateUser(s2, null, null), s3);
        }
