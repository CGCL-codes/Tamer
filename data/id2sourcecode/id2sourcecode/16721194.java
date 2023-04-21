    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int mapid = slea.readInt();
        MapleCharacter player = c.getPlayer();
        if (player.getMap().getId() != mapid) {
            log.warn("Player: " + player.getName() + " is trying to find out if boat is there without being in the map he says.");
        }
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
        if (map.hasBoat() == 2 && !player.GetBoatHere()) {
            player.toggleBoatHere();
            c.getSession().write((MaplePacketCreator.boatPacket(true)));
            return;
        } else if (map.hasBoat() == 1 && (mapid != 200090000 || mapid != 200090010) && player.GetBoatHere()) {
            player.toggleBoatHere();
            c.getSession().write(MaplePacketCreator.boatPacket(false));
            return;
        }
    }
