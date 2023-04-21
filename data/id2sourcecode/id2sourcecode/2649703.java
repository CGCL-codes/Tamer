    public static void broadCastTV(boolean active_) {
        WorldChannelInterface wci = user.getClient().getChannelServer().getWorldInterface();
        setActive(active_);
        try {
            if (active_) {
                wci.broadcastMessage(null, MaplePacketCreator.enableTV().getBytes());
                wci.broadcastMessage(null, startTV().getBytes());
                scheduleCancel();
            } else {
                wci.broadcastMessage(null, removeTV().getBytes());
            }
        } catch (RemoteException noob) {
            c.getChannelServer().reconnectWorld();
        }
    }
