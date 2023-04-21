    public boolean useUserCommand(int id, L2PcInstance activeChar) {
        if (id != COMMAND_IDS[0]) return false;
        if (activeChar.isInParty()) if (activeChar.getParty().isLeader(activeChar) && activeChar.getParty().isInCommandChannel() && activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar)) {
            L2CommandChannel channel = activeChar.getParty().getCommandChannel();
            SystemMessage sm = SystemMessage.sendString("The Command Channel was disbanded.");
            channel.broadcastToChannelMembers(sm);
            channel.disbandChannel();
            return true;
        }
        return false;
    }
