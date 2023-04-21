    public void closePortal(int mapid, String pName) {
        getClient().getChannelServer().getMapFactory().getMap(mapid).getPortal(pName).setPortalState(false);
    }
