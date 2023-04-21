        public void setVoice(java.lang.String senderSpec, java.lang.String channelName, java.lang.String nickNameAffected, boolean isModeOn) {
            IRCChannel channel = (IRCChannel) getRoom(channelName);
            if (channel != null) {
                IRCChannelParticipant rp = ircController.getChannelRoleByNickName(channel, nickNameAffected);
                rp.setVoice(isModeOn);
            }
        }
