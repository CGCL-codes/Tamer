    @Override
    public boolean useUserCommand(final int id, final L2PcInstance activeChar) {
        if (id != COMMAND_IDS[0]) return false;
        if (activeChar.getParty() == null || activeChar.getParty().getCommandChannel() == null) return false;
        if (activeChar.getParty().isLeader(activeChar) && activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar)) {
            final L2CommandChannel channel = activeChar.getParty().getCommandChannel();
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.COMMAND_CHANNEL_DISBANDED);
            channel.broadcastToChannelMembers(sm);
            sm = null;
            channel.disbandChannel();
            return true;
        }
        return false;
    }
