    public void warpRandom(int mapid) {
        MapleMap target = c.getChannelServer().getMapFactory().getMap(mapid);
        Random rand = new Random();
        getPlayer().changeMap(target, target.getPortal(rand.nextInt(target.getPortals().size())));
    }
