    public void changePartyLeader(String name) {
        L2PcInstance player = getPlayerByName(name);
        if (player != null && !player.isInDuel()) {
            if (getPartyMembers().contains(player)) {
                if (isLeader(player)) {
                    player.sendPacket(new SystemMessage(SystemMessageId.YOU_CANNOT_TRANSFER_RIGHTS_TO_YOURSELF));
                } else {
                    L2PcInstance temp;
                    int p1 = getPartyMembers().indexOf(player);
                    temp = getLeader();
                    getPartyMembers().set(0, getPartyMembers().get(p1));
                    getPartyMembers().set(p1, temp);
                    SystemMessage msg = new SystemMessage(SystemMessageId.C1_HAS_BECOME_A_PARTY_LEADER);
                    msg.addString(getLeader().getName());
                    broadcastToPartyMembers(msg);
                    broadcastToPartyMembersNewLeader();
                    if (isInCommandChannel() && temp.equals(_commandChannel.getChannelLeader())) {
                        _commandChannel.setChannelLeader(getLeader());
                        msg = new SystemMessage(SystemMessageId.COMMAND_CHANNEL_LEADER_NOW_C1);
                        msg.addString(_commandChannel.getChannelLeader().getName());
                        _commandChannel.broadcastToChannelMembers(msg);
                    }
                    if (player.isInPartyMatchRoom()) {
                        PartyMatchRoom room = PartyMatchRoomList.getInstance().getPlayerRoom(player);
                        room.changeLeader(player);
                    }
                }
            } else {
                player.sendPacket(new SystemMessage(SystemMessageId.YOU_CAN_TRANSFER_RIGHTS_ONLY_TO_ANOTHER_PARTY_MEMBER));
            }
        }
    }
