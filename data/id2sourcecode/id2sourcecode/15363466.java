        public void setOperator(java.lang.String senderSpec, java.lang.String channelName, java.lang.String nickNameAffected, boolean isModeOn) {
            IRCChannel channel = (IRCChannel) getRoom(channelName);
            if (channel != null) {
                IRCChannelParticipant rp = ircController.getChannelRoleByNickName(channel, nickNameAffected);
                rp.setOp(isModeOn);
            }
        }
