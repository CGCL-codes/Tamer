    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splittedLine) throws Exception, IllegalCommandSyntaxException {
        Collection<ChannelServer> cservs = ChannelServer.getAllInstances();
        String outputMessage = StringUtil.joinStringFrom(splittedLine, 1);
        if (outputMessage.equalsIgnoreCase("!array")) outputMessage = c.getChannelServer().getArrayString();
        for (ChannelServer cserv : cservs) {
            cserv.setServerMessage(outputMessage);
        }
    }
