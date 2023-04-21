    @Override
    public void handleChat(final int type, final L2PcInstance activeChar, final String target, final String text) {
        if (activeChar.isInParty()) {
            if (activeChar.getParty().isInCommandChannel() && activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar)) {
                final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
                activeChar.getParty().getCommandChannel().broadcastCSToChannelMembers(cs, activeChar);
            }
        }
    }
