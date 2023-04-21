    public void addMessengerPlayer(MapleMessenger messenger, String namefrom, int fromchannel, int position) throws RemoteException {
        for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
            if (messengerchar.getChannel() == server.getChannel() && !(messengerchar.getName().equals(namefrom))) {
                MapleCharacter chr = server.getPlayerStorage().getCharacterByName(messengerchar.getName());
                if (chr != null) {
                    MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(namefrom);
                    chr.getClient().getSession().write(MaplePacketCreator.addMessengerPlayer(namefrom, from, position, fromchannel - 1));
                    from.getClient().getSession().write(MaplePacketCreator.addMessengerPlayer(chr.getName(), chr, messengerchar.getPosition(), messengerchar.getChannel() - 1));
                }
            } else if (messengerchar.getChannel() == server.getChannel() && (messengerchar.getName().equals(namefrom))) {
                MapleCharacter chr = server.getPlayerStorage().getCharacterByName(messengerchar.getName());
                if (chr != null) {
                    chr.getClient().getSession().write(MaplePacketCreator.joinMessenger(messengerchar.getPosition()));
                }
            }
        }
    }
