    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int charId = slea.readInt();
        int world = slea.readInt();
        c.setWorld(world);
        try {
            c.setChannel(new Random().nextInt(ChannelServer.getAllInstances().size()));
        } catch (Exception e) {
            c.setChannel(1);
        }
        try {
            if (c.getIdleTask() != null) {
                c.getIdleTask().cancel(true);
            }
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
            String channelServerIP = MapleClient.getChannelServerIPFromSubnet(c.getSession().getRemoteAddress().toString().replace("/", "").split(":")[0], c.getChannel());
            if (channelServerIP.equals("0.0.0.0")) {
                String[] socket = LoginServer.getInstance().getIP(c.getChannel()).split(":");
                c.getSession().write(MaplePacketCreator.getServerIP(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]), charId));
            } else {
                String[] socket = LoginServer.getInstance().getIP(c.getChannel()).split(":");
                c.getSession().write(MaplePacketCreator.getServerIP(InetAddress.getByName(channelServerIP), Integer.parseInt(socket[1]), charId));
            }
        } catch (UnknownHostException e) {
            log.error("Host not found", e);
        }
    }
