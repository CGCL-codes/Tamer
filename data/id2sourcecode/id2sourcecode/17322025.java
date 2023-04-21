    public void expelMember(MapleGuildCharacter initiator, String name, int cid) {
        Logger log = LoggerFactory.getLogger(this.getClass());
        synchronized (members) {
            java.util.Iterator<MapleGuildCharacter> itr = members.iterator();
            MapleGuildCharacter mgc;
            while (itr.hasNext()) {
                mgc = itr.next();
                if (mgc.getId() == cid && initiator.getGuildRank() < mgc.getGuildRank()) {
                    this.broadcast(MaplePacketCreator.memberLeft(mgc, true));
                    itr.remove();
                    bDirty = true;
                    this.broadcast(MaplePacketCreator.serverNotice(5, initiator.getName() + " has expelled " + mgc.getName() + "."));
                    try {
                        if (mgc.isOnline()) {
                            WorldRegistryImpl.getInstance().getChannel(mgc.getChannel()).setGuildAndRank(cid, 0, 5);
                        } else {
                            String sendTo = mgc.getName();
                            String sendFrom = initiator.getName();
                            String msg = "You have been expelled from the guild.";
                            try {
                                initiator.getName();
                                MaplePacketCreator.sendUnkwnNote(sendTo, msg, sendFrom);
                            } catch (SQLException e) {
                                log.error("SAVING NOTE", e);
                            }
                            WorldRegistryImpl.getInstance().getChannel(1).setOfflineGuildStatus((short) 0, (byte) 5, cid);
                        }
                    } catch (RemoteException re) {
                        re.printStackTrace();
                        return;
                    }
                    return;
                }
            }
            log.error("Unable to find member with name " + name + " and id " + cid);
        }
    }
