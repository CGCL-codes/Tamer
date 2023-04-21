    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        if (splitted[0].equals("!shutdown")) {
            int time = 60000;
            if (splitted.length > 1) {
                time = Integer.parseInt(splitted[1]) * 60000;
            }
            CommandProcessor.forcePersisting();
            c.getChannelServer().shutdown(time);
        } else if (splitted[0].equals("!shutdownworld")) {
            int time = 60000;
            if (splitted.length > 1) {
                time = Integer.parseInt(splitted[1]) * 60000;
            }
            CommandProcessor.forcePersisting();
            c.getChannelServer().shutdownWorld(time);
        } else if (splitted[0].equals("!shutdownnow")) {
            CommandProcessor.forcePersisting();
            new ShutdownServer(c.getChannel()).run();
        }
    }
