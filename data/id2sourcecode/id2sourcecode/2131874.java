    public void closeDoor(int mapid) {
        getClient().getChannelServer().getMapFactory().getMap(mapid).setReactorState();
    }
