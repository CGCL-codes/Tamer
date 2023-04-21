    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        ChannelServer cserv = c.getChannelServer();
        if (splitted[0].equals("!clearguilds")) {
            try {
                mc.dropMessage("Attempting to reload all guilds... this may take a while...");
                cserv.getWorldInterface().clearGuilds();
                mc.dropMessage("Completed.");
            } catch (RemoteException re) {
                mc.dropMessage("RemoteException occurred while attempting to reload guilds.");
                log.error("RemoteException occurred while attempting to reload guilds.", re);
            }
        } else if (splitted[0].equals("!reloadops")) {
            try {
                ExternalCodeTableGetter.populateValues(SendPacketOpcode.getDefaultProperties(), SendPacketOpcode.values());
                ExternalCodeTableGetter.populateValues(RecvPacketOpcode.getDefaultProperties(), RecvPacketOpcode.values());
            } catch (Exception e) {
                log.error("Failed to reload props", e);
            }
            PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
            PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
        } else if (splitted[0].equals("!clearPortalScripts")) {
            PortalScriptManager.getInstance().clearScripts();
        } else if (splitted[0].equals("!reloaddrops")) {
            MapleMonsterInformationProvider.getInstance().clearDrops();
        } else if (splitted[0].equals("!clearReactorDrops")) {
            ReactorScriptManager.getInstance().clearDrops();
        } else if (splitted[0].equals("!clearshops")) {
            MapleShopFactory.getInstance().clear();
        } else if (splitted[0].equals("!clearevents")) {
            for (ChannelServer instance : ChannelServer.getAllInstances()) {
                instance.reloadEvents();
            }
        } else if (splitted[0].equals("!reloadcommands")) {
            CommandProcessor.getInstance().reloadCommands();
        }
    }
