    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        L2PcInstance player = L2World.getInstance().getPlayer(_name);
        if (player == null) return;
        if (activeChar.isInParty() && player.isInParty() && activeChar.getParty().equals(player.getParty())) return;
        if (activeChar.isInParty()) {
            L2Party activeParty = activeChar.getParty();
            if (activeParty.getPartyMembers().get(0).equals(activeChar)) {
                if (activeParty.isInCommandChannel() && activeParty.getCommandChannel().getChannelLeader().equals(activeChar)) {
                    if (player.isInParty()) {
                        if (player.getParty().isInCommandChannel()) {
                            activeChar.sendMessage("Your target is already in a CommandChannel");
                        } else {
                            askJoinMPCC(activeChar, player);
                        }
                    } else {
                        activeChar.sendMessage("Your target has no Party.");
                    }
                } else if (activeParty.isInCommandChannel() && !activeParty.getCommandChannel().getChannelLeader().equals(activeChar)) {
                    activeChar.sendMessage("Only the CommandChannelLeader can give out an invite.");
                } else {
                    if (player.isInParty()) {
                        if (player.getParty().isInCommandChannel()) {
                            activeChar.sendMessage("Your target is already in a CommandChannel");
                        } else {
                            askJoinMPCC(activeChar, player);
                        }
                    } else {
                        activeChar.sendMessage("Your target has no Party.");
                    }
                }
            } else {
                activeChar.sendMessage("Only the Partyleader can give out an invite.");
            }
        }
    }
