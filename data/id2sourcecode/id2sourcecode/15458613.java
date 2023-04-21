    @SuppressWarnings("unchecked")
    public static boolean executeSuperCommand(MapleClient c, MessageCallback mc, String line, boolean show) {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        String[] splitted = line.split(" ");
        if (splitted[0].equals("!checkkarma")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (splitted.length == 1) {
                mc.dropMessage(victim + "'s Karma level is at: " + victim.getKarma());
                if (victim.getKarma() <= -50) {
                    mc.dropMessage("You may want to ban him/her for low karma.");
                } else if (victim.getKarma() >= 50) {
                    mc.dropMessage("You may want to set him/her as an intern for high karma.");
                }
            } else if (splitted.length > 1) {
                if (splitted[2].equals("ban")) {
                    if (victim.getKarma() < -49) {
                        victim.ban("Low Karma: " + victim.getKarma());
                    } else {
                        mc.dropMessage("Too much karma");
                    }
                } else if (splitted[2].equals("intern")) {
                    if (victim.getKarma() > 49) {
                        victim.setGMLevel(2);
                    } else {
                        mc.dropMessage("Not enough karma");
                    }
                    mc.dropMessage("You have set " + victim + " as an intern.");
                } else {
                    mc.dropMessage("Syntax: !checkkarma [user] [ban/intern]");
                }
            } else {
                mc.dropMessage("Syntax: !checkkarma [user]");
            }
        } else if (splitted[0].equals("!tspawns")) {
            boolean on;
            Integer cleardrops = getOptionalIntArg(splitted, 1, 0);
            mc.dropMessage("If you want the monsters to drop stuff next time use this command: !tspawn 1");
            executeSuperCommand(c, mc, "!killall " + cleardrops + " (This is an addition to !tspawns)", false);
            on = player.getMap().toggleSpawns();
            if (!on) {
                player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "Spawns have been turned off in this map. If you want them back on, please inform the GM."));
            } else {
                player.getClient().getChannelServer().getMapFactory().reloadmapmonsters(player.getMapId());
                player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "Spawns have been turned on in this map. If you want them back off, please inform the GM."));
            }
            if (cleardrops != 0 && cleardrops != 1) {
                mc.dropMessage("Invalid. Syntax:");
                mc.dropMessage("!tspawn {1 || 0}");
                mc.dropMessage("0 = Don't give drops.");
                mc.dropMessage("1 = Give drops.");
            }
        } else if (splitted[0].equals("!morespawns")) {
            player.getClient().getChannelServer().getMapFactory().reloadmapmonsters(player.getMapId());
            mc.dropMessage("Done.");
        } else if (splitted[0].equals("!givepersonexpmulti")) {
            MapleCharacter other;
            String add = "has";
            if (splitted[1].equals("me")) {
                other = player;
                splitted[1] = "You";
                add = "have";
            } else {
                other = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            }
            other.multiplyEXPRateby(Integer.valueOf(getOptionalIntArg(splitted, 2, 2)));
            mc.dropMessage(splitted[1] + " now " + add + " an exp multiplier of: " + other.getEXPRate());
        } else if (splitted[0].equals("!dcall")) {
            for (MapleCharacter everyone : cserv.getPlayerStorage().getAllCharacters()) {
                if (everyone != player) {
                    everyone.getClient().getSession().close();
                }
                everyone.saveToDB(true);
                cserv.removePlayer(everyone);
            }
        } else if (splitted[0].equals("!givedonatorpoint")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).gainDonatorPoints(Integer.parseInt(splitted[2]));
            mc.dropMessage("You have given " + splitted[1] + " " + splitted[2] + " donator points.");
        } else if (splitted[0].equals("!horntail")) {
            for (int i = 8810002; i < 8810010; i++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(i), player.getPosition(), false);
            }
        } else if (splitted[0].equals("!npc")) {
            int npcId = Integer.parseInt(splitted[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
                npc.setPosition(player.getPosition());
                npc.setCy(player.getPosition().y);
                npc.setRx0(player.getPosition().x + 50);
                npc.setRx1(player.getPosition().x - 50);
                npc.setFh(player.getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                npc.setCustom(true);
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                mc.dropMessage("You have entered an invalid Npc-Id");
            }
        } else if (splitted[0].equals("!removenpcs")) {
            List<MapleMapObject> npcs = player.getMap().getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.NPC));
            for (MapleMapObject npcmo : npcs) {
                MapleNPC npc = (MapleNPC) npcmo;
                if (npc.isCustom()) {
                    player.getMap().removeMapObject(npc.getObjectId());
                }
            }
        } else if (splitted[0].equals("!ringme")) {
            int itemId = Integer.parseInt(splitted[1]);
            if (itemId < 111200 || itemId > 1120000 || (itemId > 1112006 && itemId < 1112800) || itemId == 1112808) {
                mc.dropMessage("Invalid itemID.");
            } else {
                int[] ret = MapleRing.createRing(c, itemId, player.getId(), player.getName(), MapleCharacter.getIdByName(splitted[2]), splitted[2]);
                if (ret[0] == -1 || ret[1] == -1) {
                    mc.dropMessage("Make sure the person you are attempting to create a ring with is online.");
                }
            }
        } else if (splitted[0].equals("!sendgmpolice")) {
            mc.dropMessage("Sending police. =3...");
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int greason = Integer.parseInt(splitted[2]);
            int duration = Integer.parseInt(splitted[3]);
            String sreadon = StringUtil.joinStringFrom(splitted, 4);
            mc.dropMessage("GReason: " + greason);
            mc.dropMessage("Duration: " + duration);
            mc.dropMessage("SReason: " + sreadon);
            victim.getClient().getSession().write(MaplePacketCreator.sendGMPolice(greason, sreadon, duration));
        } else if (splitted[0].equals("!sendhint")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            String stringthis = StringUtil.joinStringFrom(splitted, 4);
            int width = Integer.parseInt(splitted[2]);
            int height = Integer.parseInt(splitted[3]);
            victim.getClient().getSession().write(MaplePacketCreator.sendHint(stringthis, width, height));
        } else if (splitted[0].equals("!sendnote")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            String stringthis = StringUtil.joinStringFrom(splitted, 3);
            try {
                MaplePacketCreator.sendUnkwnNote(splitted[1], splitted[2], stringthis);
            } catch (SQLException e) {
                System.out.println("SQLException: " + e);
            }
        } else if (splitted[0].equals("!sex")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            String type = splitted[2], text = StringUtil.joinStringFrom(splitted, 3);
            int itemID = 5390000;
            if (type.equals("love")) {
                itemID += 2;
            } else if (type.equals("cloud")) {
                itemID++;
            }
            String[] lines = { "", "", "", "" };
            if (text.length() > 30) {
                lines[0] = text.substring(0, 10);
                lines[1] = text.substring(10, 20);
                lines[2] = text.substring(20, 30);
                lines[3] = text.substring(30);
            } else if (text.length() > 20) {
                lines[0] = text.substring(0, 10);
                lines[1] = text.substring(10, 20);
                lines[2] = text.substring(20);
            } else if (text.length() > 10) {
                lines[0] = text.substring(0, 10);
                lines[1] = text.substring(10);
            } else if (text.length() <= 10) {
                lines[0] = text;
            }
            LinkedList list = new LinkedList();
            list.add(lines[0]);
            list.add(lines[1]);
            list.add(lines[2]);
            list.add(lines[3]);
            try {
                victim.getClient().getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.getAvatarMega(victim, victim.getClient().getChannel(), itemID, list, true).getBytes());
            } catch (Exception e) {
            }
        } else if (splitted[0].equals("!speak")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 2), victim.isGM() && c.getChannelServer().allowGmWhiteText(), 0));
        } else if (splitted[0].equals("!unban")) {
            MapleCharacter.unban(splitted[1], false);
            MapleCharacter.unbanIP(splitted[1]);
            mc.dropMessage("Unbanned " + splitted[1]);
        } else if (splitted[0].equals("!zakum")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8800000), player.getPosition(), false);
            for (int x = 8800003; x <= 8800010; x++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(x), player.getPosition(), false);
            }
        } else {
            if (c.getPlayer().gmLevel() == 4) {
                if (show) mc.dropMessage("SuperGM Command " + splitted[0] + " does not exist");
            }
            return false;
        }
        return true;
    }
