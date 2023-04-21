    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        L2PcInstance player = L2World.getInstance().getPlayer(_name);
        if (player == null) return;
        if (activeChar.isInParty() && player.isInParty() && activeChar.getParty().equals(player.getParty())) return;
        SystemMessage sm;
        if (activeChar.isInParty()) {
            L2Party activeParty = activeChar.getParty();
            if (activeParty.getLeader().equals(activeChar)) {
                if (activeParty.isInCommandChannel() && activeParty.getCommandChannel().getChannelLeader().equals(activeChar)) {
                    if (player.isInParty()) {
                        if (player.getParty().isInCommandChannel()) {
                            sm = new SystemMessage(SystemMessageId.C1_ALREADY_MEMBER_OF_COMMAND_CHANNEL);
                            sm.addString(player.getName());
                            activeChar.sendPacket(sm);
                        } else {
                            askJoinMPCC(activeChar, player);
                        }
                    } else {
                        activeChar.sendMessage("Your target has no Party.");
                    }
                } else if (activeParty.isInCommandChannel() && !activeParty.getCommandChannel().getChannelLeader().equals(activeChar)) {
                    sm = new SystemMessage(SystemMessageId.CANNOT_INVITE_TO_COMMAND_CHANNEL);
                    activeChar.sendPacket(sm);
                } else {
                    if (player.isInParty()) {
                        if (player.getParty().isInCommandChannel()) {
                            sm = new SystemMessage(SystemMessageId.C1_ALREADY_MEMBER_OF_COMMAND_CHANNEL);
                            sm.addString(player.getName());
                            activeChar.sendPacket(sm);
                        } else {
                            askJoinMPCC(activeChar, player);
                        }
                    } else {
                        activeChar.sendMessage("Your target has no Party.");
                    }
                }
            } else {
                sm = new SystemMessage(SystemMessageId.CANNOT_INVITE_TO_COMMAND_CHANNEL);
                activeChar.sendPacket(sm);
            }
        }
    }
