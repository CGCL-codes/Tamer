    public void createMapMonitor(int mapId, boolean closePortal, int portalMap, String portalName, int reactorMap, int reactor) {
        MaplePortal portal = null;
        if (closePortal) {
            portal = c.getChannelServer().getMapFactory().getMap(portalMap).getPortal(portalName);
            portal.setPortalStatus(MaplePortal.CLOSED);
        }
        MapleReactor r = null;
        if (reactor > -1) {
            r = c.getChannelServer().getMapFactory().getMap(reactorMap).getReactorById(reactor);
            r.setState((byte) 1);
            c.getChannelServer().getMapFactory().getMap(reactorMap).broadcastMessage(MaplePacketCreator.triggerReactor(r, 1));
        }
        new MapMonitor(c.getChannelServer().getMapFactory().getMap(mapId), closePortal ? portal : null, c.getChannel(), r);
    }
