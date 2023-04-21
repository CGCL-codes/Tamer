    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        slea.readInt();
        slea.readShort();
        int itemid = slea.readInt();
        int newexp = expRandom(c.getPlayer().getMount().getLevel()) * ChannelServer.getInstance(c.getChannel()).getMountRate();
        int oldexp = c.getPlayer().getMount().getExp();
        if (c.getPlayer().getInventory(MapleInventoryType.USE).findById(itemid) != null) {
            if (c.getPlayer().getMount() != null) {
                int oldtiredness = c.getPlayer().getMount().getTiredness();
                c.getPlayer().getMount().setTiredness(c.getPlayer().getMount().getTiredness() - 30);
                c.getPlayer().getMount().setExp(newexp + oldexp);
                int level = c.getPlayer().getMount().getLevel();
                if (c.getPlayer().getMount().getExp() >= ExpTable.getMountExpNeededForLevel(level) && level < 31 && oldtiredness != 0) {
                    c.getPlayer().getMount().setLevel(level + 1);
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.updateMount(c.getPlayer().getId(), c.getPlayer().getMount(), true));
                } else {
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.updateMount(c.getPlayer().getId(), c.getPlayer().getMount(), false));
                }
                MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemid, 1, true, false);
            } else {
                c.getPlayer().dropMessage("Please get on your mount first before using the mount food.");
            }
        } else {
        }
    }
