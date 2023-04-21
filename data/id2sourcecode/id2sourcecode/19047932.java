    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        byte mode = slea.readByte();
        MapleCharacter player = c.getPlayer();
        switch(mode) {
            case 0x00:
                String partnerName = slea.readMapleAsciiString();
                MapleCharacter partner = c.getChannelServer().getPlayerStorage().getCharacterByName(partnerName);
                if (partnerName.equalsIgnoreCase(player.getName())) {
                    c.getSession().write(net.sf.odinms.tools.MaplePacketCreator.serverNotice(1, "You cannot put your own name in it."));
                    return;
                } else if (partner == null) {
                    c.getSession().write(net.sf.odinms.tools.MaplePacketCreator.serverNotice(1, partnerName + " was not found on this channel. If you are both logged in, please make sure you are in the same channel."));
                    return;
                } else if (partner.getGender() == player.getGender()) {
                    c.getSession().write(net.sf.odinms.tools.MaplePacketCreator.serverNotice(1, "Your partner is the same gender as you."));
                    return;
                } else if (player.isMarried() == 0 && partner.isMarried() == 0) {
                    NPCScriptManager.getInstance().start(partner.getClient(), 9201002, "marriagequestion", player);
                }
                break;
            case 0x01:
                c.getSession().write(net.sf.odinms.tools.MaplePacketCreator.serverNotice(1, "You've cancelled the request."));
                break;
            case 0x03:
                if (player.getPartner() != null) {
                    Marriage.divorceEngagement(player, player.getPartner());
                    c.getSession().write(net.sf.odinms.tools.MaplePacketCreator.serverNotice(1, "Your engagement has been broken up."));
                    break;
                } else {
                    log.info("Failed canceling engagement..");
                    break;
                }
            default:
                log.info("Unhandled Ring Packet : " + slea.toString());
                break;
        }
    }
