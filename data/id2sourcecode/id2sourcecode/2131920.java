    public void removeSquadMember(MapleSquadType type, int index, boolean ban) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            MapleCharacter chr = squad.getMembers().get(index);
            squad.banMember(chr, ban);
        }
    }
