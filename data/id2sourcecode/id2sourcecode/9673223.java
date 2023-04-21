    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        ChannelServer cserv = c.getChannelServer();
        if (splitted[0].equals("!warp")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (splitted.length == 2) {
                    MapleMap target = victim.getMap();
                    c.getPlayer().changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
                } else {
                    int mapid = Integer.parseInt(splitted[2]);
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(mapid);
                    victim.changeMap(target, target.getPortal(0));
                }
            } else {
                try {
                    victim = c.getPlayer();
                    WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
                    if (loc != null) {
                        mc.dropMessage("You will be cross-channel warped. This may take a few seconds.");
                        MapleMap target = c.getChannelServer().getMapFactory().getMap(loc.map);
                        String ip = c.getChannelServer().getIP(loc.channel);
                        c.getPlayer().getMap().removePlayer(c.getPlayer());
                        victim.setMap(target);
                        String[] socket = ip.split(":");
                        if (c.getPlayer().getTrade() != null) {
                            MapleTrade.cancelTrade(c.getPlayer());
                        }
                        try {
                            WorldChannelInterface wci = ChannelServer.getInstance(c.getChannel()).getWorldInterface();
                            wci.addBuffsToStorage(c.getPlayer().getId(), c.getPlayer().getAllBuffs());
                            wci.addCooldownsToStorage(c.getPlayer().getId(), c.getPlayer().getAllCooldowns());
                        } catch (RemoteException e) {
                            c.getChannelServer().reconnectWorld();
                        }
                        c.getPlayer().saveToDB(true);
                        if (c.getPlayer().getCheatTracker() != null) c.getPlayer().getCheatTracker().dispose();
                        ChannelServer.getInstance(c.getChannel()).removePlayer(c.getPlayer());
                        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
                        try {
                            MaplePacket packet = MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]));
                            c.getSession().write(packet);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        int map = Integer.parseInt(splitted[1]);
                        MapleMap target = cserv.getMapFactory().getMap(map);
                        c.getPlayer().changeMap(target, target.getPortal(0));
                    }
                } catch (Exception e) {
                    mc.dropMessage("Something went wrong " + e.getMessage());
                }
            }
        } else if (splitted[0].equals("!warphere")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestSpawnpoint(c.getPlayer().getPosition()));
        } else if (splitted[0].equals("!jail")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int mapid = 200090300;
            if (splitted.length > 2 && splitted[1].equals("2")) {
                mapid = 980000404;
                victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
            }
            if (victim != null) {
                MapleMap target = cserv.getMapFactory().getMap(mapid);
                MaplePortal targetPortal = target.getPortal(0);
                victim.changeMap(target, targetPortal);
                mc.dropMessage(victim.getName() + " was jailed!");
            } else {
                mc.dropMessage(splitted[1] + " not found!");
            }
        } else if (splitted[0].equals("!map")) {
            int mapid = Integer.parseInt(splitted[1]);
            MapleMap target = cserv.getMapFactory().getMap(mapid);
            MaplePortal targetPortal = null;
            if (splitted.length > 2) {
                try {
                    targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                } catch (IndexOutOfBoundsException ioobe) {
                } catch (NumberFormatException nfe) {
                }
            }
            if (targetPortal == null) {
                targetPortal = target.getPortal(0);
            }
            c.getPlayer().changeMap(target, targetPortal);
        } else if (splitted[0].equals("!exprate")) {
            if (splitted.length > 1) {
                int exp = Integer.parseInt(splitted[1]);
                cserv.setExpRate(exp);
                MaplePacket packet = MaplePacketCreator.serverNotice(6, "Exprate has been changed to " + exp + "x");
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else {
                mc.dropMessage("Syntax: !exprate <number>");
            }
        } else if (splitted[0].equals("!say")) {
            if (splitted.length > 1) {
                MaplePacket packet = MaplePacketCreator.serverNotice(6, "[" + c.getPlayer().getName() + "] " + StringUtil.joinStringFrom(splitted, 1));
                try {
                    ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(c.getPlayer().getName(), packet.getBytes());
                } catch (RemoteException e) {
                    c.getChannelServer().reconnectWorld();
                }
            } else {
                mc.dropMessage("Syntax: !say <message>");
            }
        } else if (splitted[0].equals("!droprate")) {
            if (splitted.length > 1) {
                int drop = Integer.parseInt(splitted[1]);
                cserv.setDropRate(drop);
                MaplePacket packet = MaplePacketCreator.serverNotice(6, "Drop Rate has been changed to " + drop + "x");
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else {
                mc.dropMessage("Syntax: !droprate <number>");
            }
        } else if (splitted[0].equals("!bossdroprate")) {
            if (splitted.length > 1) {
                int bossdrop = Integer.parseInt(splitted[1]);
                cserv.setBossDropRate(bossdrop);
                MaplePacket packet = MaplePacketCreator.serverNotice(6, "Boss Drop Rate has been changed to " + bossdrop + "x");
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else {
                mc.dropMessage("Syntax: !bossdroprate <number>");
            }
        } else {
            mc.dropMessage("GM Command " + splitted[0] + " does not exist");
        }
    }
