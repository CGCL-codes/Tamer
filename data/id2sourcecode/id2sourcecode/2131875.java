    public void openDoor(int mapid) {
        getClient().getChannelServer().getMapFactory().getMap(mapid).resetReactors();
    }
