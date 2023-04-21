    public void messengerChat(MapleMessenger messenger, String chattext, String namefrom) throws RemoteException {
        int totalinchat = messenger.getMembers().size();
        MapleCharacter sender = server.getPlayerStorage().getCharacterByName(namefrom);
        if (!chattext.equals(namefrom + "1") && !chattext.equals(namefrom + "0")) {
            String[] splitted = chattext.split(" ");
            if (splitted[2].startsWith("!") || splitted[2].startsWith("@")) {
                String command = StringUtil.joinStringFrom(splitted, 2);
                if (!CommandProcessor.processCommand(sender.getClient(), command, false)) {
                    sender.getClient().getSession().write(MaplePacketCreator.messengerChat(namefrom + " : " + "Command not found: " + chattext));
                } else {
                    sender.getClient().getSession().write(MaplePacketCreator.messengerChat(namefrom + " : " + "Command executed: " + chattext));
                }
            } else {
            }
        } else {
        }
        for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
            if (messengerchar.getChannel() == server.getChannel() && !(messengerchar.getName().equals(namefrom))) {
                MapleCharacter chr = server.getPlayerStorage().getCharacterByName(messengerchar.getName());
                if (chr != null) {
                    chr.getClient().getSession().write(MaplePacketCreator.messengerChat(chattext));
                }
            }
        }
    }
