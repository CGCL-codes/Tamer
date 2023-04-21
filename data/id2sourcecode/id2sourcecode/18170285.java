    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        if (splitted[0].equals("!mapplayers")) {
            MessageCallback callback = new ServernoticeMapleClientMessageCallback(c);
            StringBuilder builder = new StringBuilder("Players on Map: ");
            for (MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
                if (builder.length() > 150) {
                    builder.setLength(builder.length() - 2);
                    callback.dropMessage(builder.toString());
                    builder = new StringBuilder();
                }
                builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                builder.append(", ");
            }
            builder.setLength(builder.length() - 2);
            c.getSession().write(MaplePacketCreator.serverNotice(6, builder.toString()));
        } else if (splitted[0].equals("!cheaters")) {
            try {
                List<CheaterData> cheaters = c.getChannelServer().getWorldInterface().getCheaters();
                for (int x = cheaters.size() - 1; x >= 0; x--) {
                    CheaterData cheater = cheaters.get(x);
                    mc.dropMessage(cheater.getInfo());
                }
            } catch (RemoteException e) {
                c.getChannelServer().reconnectWorld();
            }
        }
    }
