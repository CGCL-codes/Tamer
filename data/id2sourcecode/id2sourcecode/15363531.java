    public void handleModeChangeRaw(String senderSpecification, String s, String modeFor, Vector vector) {
        IRCChannel channel = (IRCChannel) getRoom(s);
        if (channel != null) {
            IRCChannelParticipant rp = ircController.getChannelRoleByNickName(channel, modeFor);
            rp.modeChange(senderSpecification, modeFor, vector);
        } else {
        }
    }
