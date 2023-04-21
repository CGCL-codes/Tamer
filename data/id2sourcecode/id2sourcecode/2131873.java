    public void makeRing(String partner, int ringId) {
        net.sf.odinms.client.MapleRing.createRing(c, ringId, c.getPlayer().getId(), c.getPlayer().getName(), c.getChannelServer().getPlayerStorage().getCharacterByName(partner).getId(), partner);
    }
