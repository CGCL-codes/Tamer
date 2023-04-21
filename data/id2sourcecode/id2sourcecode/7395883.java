    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        if (slea.available() == 0) {
            int channel = c.getChannel();
            String ip = ChannelServer.getInstance(c.getChannel()).getIP(channel);
            String[] socket = ip.split(":");
            c.getPlayer().saveToDB(true);
            c.getPlayer().setInCS(false);
            ChannelServer.getInstance(c.getChannel()).removePlayer(c.getPlayer());
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
            try {
                MaplePacket packet = MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]));
                c.getSession().write(packet);
                c.getSession().close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            byte type = slea.readByte();
            int targetid = slea.readInt();
            String startwp = slea.readMapleAsciiString();
            MaplePortal portal = c.getPlayer().getMap().getPortal(startwp);
            MapleCharacter player = c.getPlayer();
            if (targetid != -1 && !c.getPlayer().isAlive()) {
                boolean executeStandardPath = true;
                if (player.getEventInstance() != null) {
                    executeStandardPath = player.getEventInstance().revivePlayer(player);
                }
                if (executeStandardPath) {
                    c.getPlayer().cancelAllBuffs();
                    player.setHp(50);
                    MapleMap to = c.getPlayer().getMap().getReturnMap();
                    MaplePortal pto = to.getPortal(0);
                    player.setStance(0);
                    player.changeMap(to, pto);
                }
            } else if (targetid != -1 && c.getPlayer().isGM()) {
                if (c.getPlayer().getChalkboard() != null) {
                    c.getPlayer().getClient().getSession().write(MaplePacketCreator.useChalkboard(c.getPlayer(), true));
                }
                MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                MaplePortal pto = to.getPortal(0);
                player.changeMap(to, pto);
            } else if (targetid != -1 && !c.getPlayer().isGM()) {
            } else {
                if (portal != null) {
                    portal.enterPortal(c);
                } else {
                    c.getSession().write(MaplePacketCreator.enableActions());
                }
            }
        }
    }
