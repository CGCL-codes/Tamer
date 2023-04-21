    public void changeRank(int cid, int newRank) {
        for (MapleGuildCharacter mgc : members) {
            if (cid == mgc.getId()) {
                try {
                    if (mgc.isOnline()) {
                        WorldRegistryImpl.getInstance().getChannel(mgc.getChannel()).setGuildAndRank(cid, this.id, newRank);
                    } else {
                        WorldRegistryImpl.getInstance().getChannel(1).setOfflineGuildStatus((short) this.id, (byte) newRank, cid);
                    }
                } catch (RemoteException re) {
                    re.printStackTrace();
                    return;
                }
                mgc.setGuildRank(newRank);
                this.broadcast(MaplePacketCreator.changeRank(mgc));
                return;
            }
        }
        log.info("INFO: unable to find the correct id for changeRank({}, {})", cid, newRank);
    }
