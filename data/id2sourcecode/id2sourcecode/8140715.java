    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        if (splitted[0].equals("!notice")) {
            int joinmod = 1;
            int range = -1;
            if (splitted[1].equals("m")) {
                range = 0;
            } else if (splitted[1].equals("c")) {
                range = 1;
            } else if (splitted[1].equals("w")) {
                range = 2;
            }
            int tfrom = 2;
            if (range == -1) {
                range = 2;
                tfrom = 1;
            }
            int type = getNoticeType(splitted[tfrom]);
            if (type == -1) {
                type = 0;
                joinmod = 0;
            }
            String prefix = "";
            if (splitted[tfrom].equals("nv")) {
                prefix = "[Notice] ";
            }
            joinmod += tfrom;
            String outputMessage = StringUtil.joinStringFrom(splitted, joinmod);
            if (outputMessage.equalsIgnoreCase("!array")) outputMessage = c.getChannelServer().getArrayString();
            MaplePacket packet = MaplePacketCreator.serverNotice(type, prefix + outputMessage);
            if (range == 0) {
                c.getPlayer().getMap().broadcastMessage(packet);
            } else if (range == 1) {
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else if (range == 2) {
                try {
                    ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(c.getPlayer().getName(), packet.getBytes());
                } catch (RemoteException e) {
                    c.getChannelServer().reconnectWorld();
                }
            }
        } else if (splitted[0].equals("!me")) {
            String prefix = "[" + c.getPlayer().getName() + "] ";
            String message = prefix + StringUtil.joinStringFrom(splitted, 1);
            c.getChannelServer().broadcastPacket(MaplePacketCreator.serverNotice(6, message));
        }
    }
