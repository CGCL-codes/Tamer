    @Override
    public String onTalk(L2Npc npc, L2PcInstance player) {
        if (npc.getNpcId() == CUBE) {
            int x = 150037 + Rnd.get(500);
            int y = -57720 + Rnd.get(500);
            player.teleToLocation(x, y, -2976);
            return null;
        }
        String htmltext = "";
        if (GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == DEAD) {
            htmltext = "<html><body>There is nothing beyond the Magic Force Field. Come back later.<br>(You may not enter because Frintezza is not inside the Imperial Tomb.)</body></html>";
        } else if (GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == DORMANT) {
            if ((!player.isInParty() || !player.getParty().isLeader(player)) || (player.getParty().getCommandChannel() == null) || (player.getParty().getCommandChannel().getChannelLeader() != player)) {
                htmltext = "<html><body>No reaction. Contact must be initiated by the Command Channel Leader.</body></html>";
            } else if (player.getParty().getCommandChannel().getPartys().size() < 4 || player.getParty().getCommandChannel().getPartys().size() > 5) {
                htmltext = "<html><body>Your command channel needs to have at least 4 parties and a maximum of 5.</body></html>";
            } else if (player.getInventory().getItemByItemId(8073) == null) {
                htmltext = "<html><body>You dont have required item.</body></html>";
            } else {
                player.destroyItemByItemId("Quest", 8073, 1, player, true);
                L2CommandChannel CC = player.getParty().getCommandChannel();
                GrandBossManager.getInstance().setBossStatus(FRINTEZZA, WAITING);
                startQuestTimer("close", 0, npc, null);
                startQuestTimer("room1_spawn", 5000, npc, null);
                startQuestTimer("room_final", 2100000, npc, null);
                startQuestTimer("frintezza_despawn", 60000, npc, null, true);
                _LastAction = System.currentTimeMillis();
                for (L2Party party : CC.getPartys()) {
                    if (party == null) continue;
                    for (L2PcInstance member : party.getPartyMembers()) {
                        if (member == null || member.getLevel() < 74) continue;
                        if (!member.isInsideRadius(npc, 700, false, false)) continue;
                        if (_PlayersInside.size() > 45) {
                            member.sendMessage("The number of challenges have been full, so can not enter.");
                            break;
                        }
                        _PlayersInside.add(member);
                        _Zone.allowPlayerEntry(member, 300);
                        member.teleToLocation(getXFix(_invadeLoc[_LocCycle][0]) + Rnd.get(50), getYFix(_invadeLoc[_LocCycle][1]) + Rnd.get(50), getZFix(_invadeLoc[_LocCycle][2]));
                    }
                    if (_PlayersInside.size() > 45) break;
                    _LocCycle++;
                    if (_LocCycle >= 6) _LocCycle = 1;
                }
            }
        } else htmltext = "<html><body>Someone else is already inside the Magic Force Field. Try again later.</body></html>";
        return htmltext;
    }
