    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int type = slea.readByte();
        int numRecipients = slea.readByte();
        int recipients[] = new int[numRecipients];
        for (int i = 0; i < numRecipients; i++) {
            recipients[i] = slea.readInt();
        }
        String chattext = slea.readMapleAsciiString();
        MapleCharacter player = c.getPlayer();
        try {
            if (type == 0) {
                c.getChannelServer().getWorldInterface().buddyChat(recipients, player.getId(), player.getName(), chattext);
            } else if (type == 1 && player.getParty() != null) {
                c.getChannelServer().getWorldInterface().partyChat(player.getParty().getId(), chattext, player.getName());
            } else if (type == 2 && player.getGuildId() > 0) {
                c.getChannelServer().getWorldInterface().guildChat(c.getPlayer().getGuildId(), c.getPlayer().getName(), c.getPlayer().getId(), chattext);
            }
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
    }
