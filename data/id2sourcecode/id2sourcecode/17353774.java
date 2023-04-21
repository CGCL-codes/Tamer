    public void messengerInvite(String sender, int messengerid, String target, int fromchannel) throws RemoteException {
        if (isConnected(target)) {
            MapleMessenger messenger = server.getPlayerStorage().getCharacterByName(target).getMessenger();
            if (messenger == null) {
                server.getPlayerStorage().getCharacterByName(target).getClient().getSession().write(MaplePacketCreator.messengerInvite(sender, messengerid));
                MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(sender);
                from.getClient().getSession().write(MaplePacketCreator.messengerNote(target, 4, 1));
            } else {
                MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(sender);
                from.getClient().getSession().write(MaplePacketCreator.messengerChat(sender + " : " + target + " is already using Maple Messenger"));
            }
        }
    }
