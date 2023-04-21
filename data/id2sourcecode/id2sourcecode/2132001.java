    public void warpAllInMap(int mapid, int portal) {
        MapleMapFactory mapFactory = ChannelServer.getInstance(c.getChannel()).getMapFactory();
        MapleMap outMap = mapFactory.getMap(mapid);
        for (MapleCharacter aaa : outMap.getCharacters()) {
            mapFactory = ChannelServer.getInstance(aaa.getClient().getChannel()).getMapFactory();
            aaa.getClient().getPlayer().changeMap(outMap, outMap.getPortal(portal));
            outMap = mapFactory.getMap(mapid);
            aaa.getClient().getPlayer().getEventInstance().unregisterPlayer(aaa.getClient().getPlayer());
        }
    }
