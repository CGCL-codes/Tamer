    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        if (splitted[0].equals("!saveall")) {
            ChannelServer cserv = c.getChannelServer();
            for (ChannelServer chan : cserv.getAllInstances()) {
                for (MapleCharacter chr : chan.getPlayerStorage().getAllCharacters()) {
                    chr.saveToDB(true);
                }
            }
            mc.dropMessage("Save complete.");
        }
    }
