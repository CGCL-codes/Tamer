    public void handleChat(int type, L2PcInstance activeChar, String target, String text) {
        if (activeChar.isInParty()) if (activeChar.getParty().isInCommandChannel() && activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar)) {
            CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
            activeChar.getParty().getCommandChannel().broadcastToChannelMembers(cs);
        }
    }
