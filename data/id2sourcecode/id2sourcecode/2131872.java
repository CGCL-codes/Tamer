    public void openPortal(int mapid, String pName) {
        getClient().getChannelServer().getMapFactory().getMap(mapid).getPortal(pName).setPortalState(true);
    }
