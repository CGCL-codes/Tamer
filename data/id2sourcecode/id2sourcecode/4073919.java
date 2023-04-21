    public boolean useUserCommand(int id, L2PcInstance activeChar) {
        if (id != COMMAND_IDS[0]) return false;
        L2CommandChannel channel = activeChar.getParty().getCommandChannel();
        activeChar.sendMessage("================");
        activeChar.sendMessage("Command Channel Information is not fully implemented now.");
        activeChar.sendMessage("There are " + channel.getPartys().size() + " Party's in the Channel.");
        activeChar.sendMessage(channel.getMemberCount() + " Players overall.");
        activeChar.sendMessage("Leader is " + channel.getChannelLeader().getName() + ".");
        activeChar.sendMessage("Partyleader, Membercount:");
        for (L2Party party : channel.getPartys()) activeChar.sendMessage(party.getPartyMembers().get(0).getName() + ", " + party.getMemberCount());
        activeChar.sendMessage("================");
        return true;
    }
