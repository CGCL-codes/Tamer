    public static boolean executeInternCommand(MapleClient c, MessageCallback mc, String line, boolean show) {
        ChannelServer cserv = c.getChannelServer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!cheaters")) {
            try {
                List<CheaterData> cheaters = cserv.getWorldInterface().getCheaters();
                for (int x = cheaters.size() - 1; x >= 0; x--) {
                    mc.dropMessage(cheaters.get(x).getInfo());
                }
            } catch (RemoteException e) {
                cserv.reconnectWorld();
            }
        } else if (splitted[0].equals("!glimmer")) {
            MapleShopFactory.getInstance().getShop(1338).sendShop(c);
        } else if (splitted[0].equals("!gmshop")) {
            MapleShopFactory.getInstance().getShop(1337).sendShop(c);
        } else if (splitted[0].equals("!map")) {
            MapleMap target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
            c.getPlayer().changeMap(target, target.getPortal(0));
        } else if (splitted[0].equals("!maple")) {
            MapleShopFactory.getInstance().getShop(1353).sendShop(c);
        } else if (splitted[0].equals("!misc")) {
            MapleShopFactory.getInstance().getShop(1339).sendShop(c);
        } else if (splitted[0].equals("!sbag")) {
            MapleShopFactory.getInstance().getShop(1341).sendShop(c);
        } else if (splitted[0].equals("!scroll")) {
            MapleShopFactory.getInstance().getShop(1340).sendShop(c);
        } else {
            if (c.getPlayer().gmLevel() == 2) {
                if (show) mc.dropMessage("Intern Command " + splitted[0] + " does not exist");
            }
            return false;
        }
        return true;
    }
