    public void killAllMonster(int mapid) {
        getClient().getChannelServer().getMapFactory().getMap(mapid).killAllMonsters(true);
    }
