    public void shutdown(int time) throws RemoteException {
        server.broadcastPacket(MaplePacketCreator.serverNotice(0, "The world will be shut down in " + (time / 60000) + " minutes, please log off safely"));
        TimerManager.getInstance().schedule(new ShutdownServer(server.getChannel()), time);
    }
