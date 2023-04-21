    protected void updateCommand(InCommand command_o) {
        if (command_o instanceof KickCommand) {
            KickCommand kickCommand = (KickCommand) command_o;
            if (kickCommand.kickedUs(getConnection().getClientState())) {
                if (Channel.areEqual(kickCommand.getChannel(), channel)) {
                    performJoin();
                } else {
                }
            }
        } else if (command_o instanceof GenericJoinError) {
            GenericJoinError joinErr = (GenericJoinError) command_o;
            if (Channel.areEqual(joinErr.getChannel(), channel)) {
                scheduleJoin();
            }
        } else if (command_o instanceof InviteCommand) {
            InviteCommand invite = (InviteCommand) command_o;
            if (!getConnection().getClientState().isOnChannel(invite.getChannel())) {
                performJoin();
            }
        }
    }
