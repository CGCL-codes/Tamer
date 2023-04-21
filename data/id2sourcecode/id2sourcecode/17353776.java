    public void removeMessengerPlayer(MapleMessenger messenger, int position) throws RemoteException {
        for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
            if (messengerchar.getChannel() == server.getChannel()) {
                MapleCharacter chr = server.getPlayerStorage().getCharacterByName(messengerchar.getName());
                if (chr != null) {
                    chr.getClient().getSession().write(MaplePacketCreator.removeMessengerPlayer(position));
                }
            }
        }
    }
