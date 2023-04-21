        public void namReplyAddNick(String nick, char modifier) throws IOException {
            Lang.ASSERT_NOT_NULL(curRoom, "curRoom");
            IRCChannelParticipant rp = ircController.getChannelRoleByNickName((IRCChannel) curRoom, nick);
            if (rp == null) rp = (IRCChannelParticipant) ircController.createDefaultRole(curRoom, ircController.getCreateUser(nick));
            switch(modifier) {
                case '+':
                    rp.setVoice(true);
                case '@':
                    rp.setOp(true);
                case '%':
                    rp.setHalfOp(true);
            }
            IRCChannel chan = (IRCChannel) curRoom;
            chan.addRoomRole(rp);
        }
