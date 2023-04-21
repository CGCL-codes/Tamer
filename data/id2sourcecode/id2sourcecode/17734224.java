    public static void visitTrade(MapleCharacter c1, MapleCharacter c2) {
        if (c1.getTrade() != null && c1.getTrade().getPartner() == c2.getTrade() && c2.getTrade() != null && c2.getTrade().getPartner() == c1.getTrade()) {
            c2.getClient().getSession().write(MaplePacketCreator.getTradePartnerAdd(c1));
            c1.getClient().getSession().write(MaplePacketCreator.getTradeStart(c1.getClient(), c1.getTrade(), (byte) 1));
        } else {
            c1.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "The other player has already closed the trade"));
        }
    }
