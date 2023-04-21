    public boolean checkSquadLeader(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.getLeader().getId() == getPlayer().getId();
        } else {
            return false;
        }
    }
