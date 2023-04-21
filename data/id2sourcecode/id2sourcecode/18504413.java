    @Override
    protected void runImpl() {
        L2PcInstance target = L2World.getInstance().getPlayer(_name);
        L2PcInstance activeChar = getClient().getActiveChar();
        if (target != null && target.isInParty() && activeChar.isInParty() && activeChar.getParty().isInCommandChannel() && target.getParty().isInCommandChannel() && activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar)) {
            target.getParty().getCommandChannel().removeParty(target.getParty());
            SystemMessage sm = SystemMessage.sendString("Your party was dismissed from the CommandChannel.");
            target.getParty().broadcastToPartyMembers(sm);
            sm = SystemMessage.sendString(target.getParty().getPartyMembers().get(0).getName() + "'s party was dismissed from the CommandChannel.");
        } else activeChar.sendMessage("Incorrect Target");
    }
