        public void handlePart(String chan, User c, String comment) {
            String s1 = ((IRCUser) c).getActiveNick();
            if (s1 != null) if (s1.equalsIgnoreCase(getActiveNick())) {
                imListener.meParts(IRCIMNetwork.this, chan, comment);
            } else {
                final IRCChannel channel = (IRCChannel) getRoom(chan);
                channel.parts(ircController.getChannelRoleByChannelName((IRCUser) c, chan), comment);
            } else {
                Exception ex = new ExpectException("s1 is null");
                Logger.printException(ex);
            }
        }
