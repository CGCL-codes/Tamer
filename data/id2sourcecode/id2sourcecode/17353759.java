    @Override
    public void partyChat(MapleParty party, String chattext, String namefrom) throws RemoteException {
        for (MaplePartyCharacter partychar : party.getMembers()) {
            if (partychar.getChannel() == server.getChannel() && !(partychar.getName().equals(namefrom))) {
                MapleCharacter chr = server.getPlayerStorage().getCharacterByName(partychar.getName());
                if (chr != null) {
                    chr.getClient().getSession().write(MaplePacketCreator.multiChat(namefrom, chattext, 1));
                }
            }
        }
    }
