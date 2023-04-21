    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int channel = slea.readByte() + 1;
        if (!c.getPlayer().isAlive()) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        String ip = ChannelServer.getInstance(c.getChannel()).getIP(channel);
        String[] socket = ip.split(":");
        if (c.getPlayer().getTrade() != null) {
            MapleTrade.cancelTrade(c.getPlayer());
        }
        c.getPlayer().cancelMagicDoor();
        if (c.getPlayer().getBuffedValue(MapleBuffStat.MONSTER_RIDING) != null) {
            c.getPlayer().cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
        }
        if (c.getPlayer().getBuffedValue(MapleBuffStat.PUPPET) != null) {
            c.getPlayer().cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
        }
        try {
            WorldChannelInterface wci = ChannelServer.getInstance(c.getChannel()).getWorldInterface();
            wci.addBuffsToStorage(c.getPlayer().getId(), c.getPlayer().getAllBuffs());
            wci.addCooldownsToStorage(c.getPlayer().getId(), c.getPlayer().getAllCooldowns());
        } catch (RemoteException e) {
            log.info("RemoteException: {}", e);
            c.getChannelServer().reconnectWorld();
        }
        c.getPlayer().saveToDB(true);
        if (c.getPlayer().getCheatTracker() != null) c.getPlayer().getCheatTracker().dispose();
        if (c.getPlayer().getMessenger() != null) {
            MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(c.getPlayer());
            try {
                WorldChannelInterface wci = ChannelServer.getInstance(c.getChannel()).getWorldInterface();
                wci.silentLeaveMessenger(c.getPlayer().getMessenger().getId(), messengerplayer);
            } catch (RemoteException e) {
                c.getChannelServer().reconnectWorld();
            }
        }
        c.getPlayer().getMap().removePlayer(c.getPlayer());
        ChannelServer.getInstance(c.getChannel()).removePlayer(c.getPlayer());
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
        try {
            MaplePacket packet = MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]));
            c.getSession().write(packet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
