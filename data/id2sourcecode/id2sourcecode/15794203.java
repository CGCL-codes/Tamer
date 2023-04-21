    public static boolean executeAdminCommand(MapleClient c, MessageCallback mc, String line, org.slf4j.Logger log, List<Pair<MapleCharacter, String>> gmlog, Runnable persister, boolean show) {
        ChannelServer cserv = c.getChannelServer();
        MapleCharacter player = c.getPlayer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!pmob")) {
            int npcId = Integer.parseInt(splitted[1]);
            int mobTime = Integer.parseInt(splitted[2]);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (splitted[2] == null) {
                mobTime = 0;
            }
            MapleMonster mob = MapleLifeFactory.getMonster(npcId);
            if (mob != null && !mob.getName().equals("MISSINGNO")) {
                mob.setPosition(player.getPosition());
                mob.setCy(ypos);
                mob.setRx0(xpos + 50);
                mob.setRx1(xpos - 50);
                mob.setFh(fh);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid, mobtime ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "m");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.setInt(11, mobTime);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    mc.dropMessage("Failed to save MOB to the database");
                }
                player.getMap().addMonsterSpawn(mob, mobTime);
            } else {
                mc.dropMessage("You have entered an invalid Npc-Id");
            }
        } else if (splitted[0].equals("!pnpc")) {
            int npcId = Integer.parseInt(splitted[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
                npc.setPosition(player.getPosition());
                npc.setCy(ypos);
                npc.setRx0(xpos + 50);
                npc.setRx1(xpos - 50);
                npc.setFh(fh);
                npc.setCustom(true);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "n");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    mc.dropMessage("Failed to save NPC to the database");
                }
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                mc.dropMessage("You have entered an invalid Npc-Id");
            }
        } else if (splitted[0].equals("!setgmlevel")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).setGMLevel(Integer.parseInt(splitted[2]));
            mc.dropMessage("Done.");
        } else if (splitted[0].equals("!yellow")) {
            String type = "[MapleTip] ";
            type = type + StringUtil.joinStringFrom(splitted, 1);
            try {
                ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.sendYellowTip(type).getBytes());
            } catch (Exception e) {
                mc.dropMessage("Crap. Exception Caught." + e);
                cserv.reconnectWorld();
            }
        } else if (splitted[0].equals("!goonatour")) {
            final MapleClient finalc = c;
            tourschedule = TimerManager.getInstance().register(new Runnable() {

                MapleClient client = finalc;

                MapleCharacter player = client.getPlayer();

                int currentmapid = 000000000;

                int maxmapid = 999999999;

                MapleMapFactory factory = ChannelServer.getInstance(client.getChannel()).getMapFactory();

                public void run() {
                    int start = currentmapid;
                    int max = currentmapid + 2507;
                    MapleMap map = null;
                    boolean stop = false;
                    while (!stop && max > currentmapid) {
                        switch(currentmapid) {
                            case 60002:
                                currentmapid = 1000000;
                                break;
                            case 1010010:
                                currentmapid = 100000000;
                                break;
                            case 100050001:
                                currentmapid = 101000000;
                                break;
                            case 101040005:
                                currentmapid = 102000000;
                                break;
                            case 102050001:
                                currentmapid = 103000000;
                                break;
                            case 103030201:
                                currentmapid = 104000000;
                                break;
                            case 104040005:
                                currentmapid = 105030000;
                                break;
                            case 105090901:
                                currentmapid = 106000000;
                                break;
                            case 106010110:
                                currentmapid = 107000000;
                                break;
                            case 107000505:
                                currentmapid = 108000100;
                                break;
                            case 108010405:
                                currentmapid = 109010000;
                                break;
                            case 109080015:
                                currentmapid = 110000000;
                                break;
                            case 110040010:
                                currentmapid = 120000000;
                                break;
                            case 120010010:
                                currentmapid = 180000000;
                                break;
                            case 180000005:
                                currentmapid = 200000000;
                                break;
                            case 200090415:
                                currentmapid = 209000000;
                                break;
                            case 209080005:
                                currentmapid = 211000000;
                                break;
                            case 211050005:
                                currentmapid = 220000000;
                                break;
                            case 220080050:
                                currentmapid = 221000000;
                                break;
                            case 221040450:
                                currentmapid = 222000000;
                                break;
                            case 222020350:
                                currentmapid = 230000000;
                                break;
                            case 230040450:
                                currentmapid = 240000000;
                                break;
                            case 240060250:
                                currentmapid = 250000000;
                                break;
                            case 250020350:
                                currentmapid = 251000000;
                                break;
                            case 251010550:
                                currentmapid = 260000000;
                                break;
                            case 260020750:
                                currentmapid = 280010000;
                                break;
                            case 280090050:
                                currentmapid = 540000000;
                                break;
                            case 540020150:
                                currentmapid = 541000000;
                                break;
                            case 541010150:
                                currentmapid = 670000100;
                                break;
                            case 670011000:
                                currentmapid = 680000000;
                                break;
                            case 680010150:
                                currentmapid = 681000000;
                                break;
                            case 681000050:
                                currentmapid = 600000000;
                                break;
                            case 600020650:
                                currentmapid = 610010000;
                                break;
                            case 610020050:
                                currentmapid = 682000000;
                                break;
                            case 682000950:
                                currentmapid = 800000000;
                                break;
                            case 800030050:
                                currentmapid = 801000000;
                                break;
                            case 801040150:
                                currentmapid = 809000101;
                                break;
                            case 809050050:
                                currentmapid = 880000000;
                                break;
                            case 880000050:
                                currentmapid = 881000000;
                                break;
                            case 881000050:
                                currentmapid = 900000000;
                                break;
                            case 900000050:
                                currentmapid = 910000000;
                                break;
                            case 910010450:
                                currentmapid = 910200000;
                                break;
                            case 910200050:
                                currentmapid = 910300000;
                                break;
                            case 910300050:
                                currentmapid = 910500000;
                                break;
                            case 910500250:
                                currentmapid = 912000000;
                                break;
                            case 912020050:
                                currentmapid = 925010000;
                                break;
                            case 925010450:
                                currentmapid = 920010000;
                                break;
                            case 920011350:
                                currentmapid = 921100000;
                                break;
                            case 922020250:
                                currentmapid = 922200000;
                                break;
                            case 922200050:
                                currentmapid = 923000000;
                                break;
                            case 923010150:
                                currentmapid = 924000000;
                                break;
                            case 924000150:
                                currentmapid = 925100000;
                                break;
                            case 925100750:
                                currentmapid = 980000000;
                                break;
                            case 980010350:
                                currentmapid = 990000000;
                                break;
                            default:
                                currentmapid++;
                                break;
                        }
                        try {
                            if (factory.getMap(currentmapid) != null) {
                                stop = true;
                                map = factory.getMap(currentmapid);
                            } else {
                                continue;
                            }
                            if (map != null) {
                                player.dropMessage("Warping to:" + currentmapid);
                                player.changeMap(map, map.getPortal(0));
                            } else if (max <= currentmapid) {
                                player.dropMessage("Breaking at: " + currentmapid);
                            }
                        } catch (RuntimeException e) {
                            continue;
                        }
                    }
                }
            }, 1500);
        } else if (splitted[0].equals("!shutdown") || splitted[0].equals("!shutdownnow")) {
            String add = ".";
            for (MapleCharacter everyone : cserv.getPlayerStorage().getAllCharacters()) {
                everyone.saveToDB(true);
            }
            int time = 60000;
            if (splitted.length > 1) {
                time *= Integer.parseInt(splitted[1]);
                TimerManager.getInstance().register(new ShutdownAnnouncer(cserv, (long) time), 300000, 300000);
            }
            add = " in " + time + " second";
            if (time > 1) {
                add = add + "s.";
            } else {
                add = add + ".";
            }
            if (splitted[0].equals("!shutdownnow")) {
                time = 1;
                add = " now.";
            }
            for (MapleCharacter everyone : cserv.getPlayerStorage().getAllCharacters()) {
                if (everyone != c.getPlayer()) {
                    everyone.saveToDB(true);
                    everyone.dropMessage("[Notice] The server will shut down" + add);
                    everyone.getClient().disconnect();
                }
            }
            c.getPlayer().saveToDB(true);
            persister.run();
            c.getChannelServer().shutdownWorld(time);
        } else if (splitted[0].equals("!sql")) {
            try {
                String sql = StringUtil.joinStringFrom(splitted, 1);
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
                ps.executeUpdate();
                ps.close();
                mc.dropMessage("Done statement " + sql);
            } catch (SQLException e) {
                mc.dropMessage("Something wrong happened.");
            }
        } else if (splitted[0].equals("!changeenvi")) {
            Integer mode = Integer.parseInt(splitted[1]);
            String env = StringUtil.joinStringFrom(splitted, 2);
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, mode));
        } else if (splitted[0].equals("!playernpc")) {
            mc.dropMessage("Attempting to create a player NPC.");
            try {
                int charid = MapleCharacter.getIdByName(splitted[1]);
                MapleCharacter target = MapleCharacter.loadCharFromDB(charid, c, false);
                MapleCharacter targetfromcserv = ChannelServer.getInstance(player.getClient().getChannel()).getPlayerStorage().getCharacterByName(splitted[1]);
                mc.dropMessage("Finding character/NPCid");
                if (target != null || targetfromcserv != null) {
                    mc.dropMessage("Character found: creating.");
                    if (target != null) {
                        player.playerNPC(target, findOpenNPCforJob(findNPCforJob(target.getJob().getId())));
                    } else if (targetfromcserv != null) {
                        player.playerNPC(targetfromcserv, findOpenNPCforJob(findNPCforJob(target.getJob().getId())));
                    }
                } else {
                    mc.dropMessage("No character found. Going with NPCid instead.");
                    int npcid = Integer.parseInt(splitted[1]);
                    player.playerNPC(player, npcid);
                }
            } catch (SQLException e) {
                mc.dropMessage("SQLException...");
                mc.dropMessage("Message: " + e.getMessage());
            }
        } else {
            if (show) mc.dropMessage("Command " + splitted[0] + " does not exist");
            return false;
        }
        return true;
    }
