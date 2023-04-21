    public void worldMessage(int type, String message) {
        net.sf.odinms.net.MaplePacket packet = MaplePacketCreator.serverNotice(type, message);
        MapleCharacter chr = c.getPlayer();
        try {
            ChannelServer.getInstance(chr.getClient().getChannel()).getWorldInterface().broadcastMessage(chr.getName(), packet.getBytes());
        } catch (RemoteException e) {
            chr.getClient().getChannelServer().reconnectWorld();
        }
    }
