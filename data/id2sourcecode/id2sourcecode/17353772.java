    @Override
    public void reloadGuildCharacters() throws RemoteException {
        for (MapleCharacter mc : server.getPlayerStorage().getAllCharacters()) {
            if (mc.getGuildId() > 0) {
                server.getWorldInterface().setGuildMemberOnline(mc.getMGC(), true, server.getChannel());
                server.getWorldInterface().memberLevelJobUpdate(mc.getMGC());
            }
        }
        ChannelServer.getInstance(this.getChannelId()).reloadGuildSummary();
    }
