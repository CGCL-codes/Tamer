    @Override
    protected void runImpl() {
        L2PcInstance target = L2World.getInstance().getPlayer(_name);
        L2PcInstance activeChar = getClient().getActiveChar();
        if (target != null && target.isInParty() && activeChar.isInParty() && activeChar.getParty().isInCommandChannel() && target.getParty().isInCommandChannel() && activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar)) {
            if (activeChar.equals(target)) return;
            target.getParty().getCommandChannel().removeParty(target.getParty());
            SystemMessage sm = new SystemMessage(SystemMessageId.DISMISSED_FROM_COMMAND_CHANNEL);
            target.getParty().broadcastToPartyMembers(sm);
            if (activeChar.getParty().isInCommandChannel()) {
                sm = new SystemMessage(SystemMessageId.C1_PARTY_DISMISSED_FROM_COMMAND_CHANNEL);
                sm.addString(target.getParty().getLeader().getName());
                activeChar.getParty().getCommandChannel().broadcastToChannelMembers(sm);
            }
        } else {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.TARGET_CANT_FOUND));
        }
    }
