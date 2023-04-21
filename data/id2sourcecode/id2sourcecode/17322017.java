    public void broadcast(MaplePacket packet, int exceptionId, BCOp bcop) {
        WorldRegistryImpl wr = WorldRegistryImpl.getInstance();
        Set<Integer> chs = wr.getChannelServer();
        synchronized (notifications) {
            if (bDirty) {
                buildNotifications();
            }
            try {
                ChannelWorldInterface cwi;
                for (Integer ch : chs) {
                    cwi = wr.getChannel(ch);
                    if (notifications.get(ch).size() > 0) {
                        if (bcop == BCOp.DISBAND) {
                            cwi.setGuildAndRank(notifications.get(ch), 0, 5, exceptionId);
                        } else if (bcop == BCOp.EMBELMCHANGE) {
                            cwi.changeEmblem(this.id, notifications.get(ch), new MapleGuildSummary(this));
                        } else {
                            cwi.sendPacket(notifications.get(ch), packet, exceptionId);
                        }
                    }
                }
            } catch (java.rmi.RemoteException re) {
                Logger log = LoggerFactory.getLogger(this.getClass());
                log.error("Failed to contact channel(s) for broadcast.", re);
            }
        }
    }
