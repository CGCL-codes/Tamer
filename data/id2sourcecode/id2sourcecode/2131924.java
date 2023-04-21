    public void WarpTo(String player) {
        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(player);
        c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestSpawnpoint(victim.getPosition()));
    }
