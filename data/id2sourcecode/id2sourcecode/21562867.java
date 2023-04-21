    public synchronized void removePartyMember(L2PcInstance player) {
        if (getPartyMembers().contains(player)) {
            boolean isLeader = isLeader(player);
            getPartyMembers().remove(player);
            recalculatePartyLevel();
            if (player.isFestivalParticipant()) SevenSignsFestival.getInstance().updateParticipants(player, this);
            if (player.isInDuel()) DuelManager.getInstance().onRemoveFromParty(player);
            try {
                if (player.getFusionSkill() != null) player.abortCast();
                for (L2Character character : player.getKnownList().getKnownCharacters()) if (character.getFusionSkill() != null && character.getFusionSkill().getTarget() == player) character.abortCast();
            } catch (Exception e) {
            }
            SystemMessage msg = new SystemMessage(SystemMessageId.YOU_LEFT_PARTY);
            player.sendPacket(msg);
            player.sendPacket(new PartySmallWindowDeleteAll());
            player.setParty(null);
            msg = new SystemMessage(SystemMessageId.C1_LEFT_PARTY);
            msg.addString(player.getName());
            broadcastToPartyMembers(msg);
            broadcastToPartyMembers(new PartySmallWindowDelete(player));
            L2Summon summon = player.getPet();
            if (summon != null) {
                broadcastToPartyMembers(new ExPartyPetWindowDelete(summon));
            }
            if (isInDimensionalRift()) _dr.partyMemberExited(player);
            if (isInCommandChannel()) {
                player.sendPacket(new ExCloseMPCC());
            }
            if (isLeader && getPartyMembers().size() > 1) {
                msg = new SystemMessage(SystemMessageId.C1_HAS_BECOME_A_PARTY_LEADER);
                msg.addString(getLeader().getName());
                broadcastToPartyMembers(msg);
                broadcastToPartyMembersNewLeader();
            }
            if (getPartyMembers().size() == 1) {
                if (isInCommandChannel()) {
                    if (getCommandChannel().getChannelLeader().equals(getLeader())) {
                        getCommandChannel().disbandChannel();
                    } else {
                        getCommandChannel().removeParty(this);
                    }
                }
                L2PcInstance leader = getLeader();
                if (leader != null) {
                    leader.setParty(null);
                    if (leader.isInDuel()) DuelManager.getInstance().onRemoveFromParty(leader);
                }
                _members.clear();
            }
        }
    }
