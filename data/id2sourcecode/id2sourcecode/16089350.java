    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String text = slea.readMapleAsciiString();
        int show = slea.readByte();
        if (!(c.getPlayer().isGM()) && text.length() > 70) {
            AutobanManager.getInstance().autoban(c.getPlayer().getClient(), "infinite text.");
        } else {
            if (!CommandProcessor.getInstance().processCommand(c, text, true) && c.getPlayer().getCanTalk()) {
                if (c.getPlayer().getGMChat() == 0) {
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), text, false, show));
                } else if (c.getPlayer().getGMChat() == 7) {
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), text, true, show));
                } else {
                    switch(c.getPlayer().getGMChat()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.multiChat(c.getPlayer().getName(), text, c.getPlayer().getGMChat() - 1));
                            break;
                        case 5:
                        case 6:
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(c.getPlayer().getGMChat(), c.getPlayer().getName() + " : " + text));
                            break;
                        case 8:
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                            break;
                        case 9:
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.sendYellowTip(c.getPlayer().getName() + " : " + text));
                            break;
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), text, false, show));
                            c.getPlayer().dropMessage("Sorry but these chat types are currently disabled. Changing chattype back to 0");
                            c.getPlayer().setGMChat(0);
                            break;
                    }
                    if (!c.getPlayer().isHidden()) {
                        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), text, false, 1));
                    } else {
                        net.sf.odinms.server.maps.MapleMap map = c.getPlayer().getMap();
                        for (net.sf.odinms.client.MapleCharacter chr : map.getCharacters()) {
                            if (chr.isHidden() || chr.isGM()) {
                                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), text, false, 1));
                            } else {
                            }
                        }
                    }
                }
            }
        }
    }
