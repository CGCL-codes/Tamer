    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        MapleCharacter player = c.getPlayer();
        if (splitted[0].equals("!resetquest")) {
            MapleQuest.getInstance(Integer.parseInt(splitted[1])).forfeit(c.getPlayer());
        } else if (splitted[0].equals("!nearestPortal")) {
            final MaplePortal portal = player.getMap().findClosestSpawnpoint(player.getPosition());
            mc.dropMessage(portal.getName() + " id: " + portal.getId() + " script: " + portal.getScriptName());
        } else if (splitted[0].equals("!spawndebug")) {
            c.getPlayer().getMap().spawnDebug(mc);
        } else if (splitted[0].equals("!door")) {
            Point doorPos = new Point(player.getPosition());
            doorPos.y -= 270;
            MapleDoor door = new MapleDoor(c.getPlayer(), doorPos);
            door.getTarget().addMapObject(door);
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.write(HexTool.getByteArrayFromHexString("B9 00 00 47 1E 00 00 0A 04 76 FF"));
            c.getSession().write(mplew.getPacket());
            mplew = new MaplePacketLittleEndianWriter();
            mplew.write(HexTool.getByteArrayFromHexString("36 00 00 EF 1C 0D 4C 3E 1D 0D 0A 04 76 FF"));
            c.getSession().write(mplew.getPacket());
            c.getSession().write(MaplePacketCreator.enableActions());
            door = new MapleDoor(door);
            door.getTown().addMapObject(door);
        } else if (splitted[0].equals("!timerdebug")) {
            TimerManager.getInstance().dropDebugInfo(mc);
        } else if (splitted[0].equals("!threads")) {
            Thread[] threads = new Thread[Thread.activeCount()];
            Thread.enumerate(threads);
            String filter = "";
            if (splitted.length > 1) {
                filter = splitted[1];
            }
            for (int i = 0; i < threads.length; i++) {
                String tstring = threads[i].toString();
                if (tstring.toLowerCase().indexOf(filter.toLowerCase()) > -1) {
                    mc.dropMessage(i + ": " + tstring);
                }
            }
        } else if (splitted[0].equals("!showtrace")) {
            if (splitted.length < 2) {
                throw new IllegalCommandSyntaxException(2);
            }
            Thread[] threads = new Thread[Thread.activeCount()];
            Thread.enumerate(threads);
            Thread t = threads[Integer.parseInt(splitted[1])];
            mc.dropMessage(t.toString() + ":");
            for (StackTraceElement elem : t.getStackTrace()) {
                mc.dropMessage(elem.toString());
            }
        } else if (splitted[0].equals("!fakerelog")) {
            c.getSession().write(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
        } else if (splitted[0].equals("!toggleoffense")) {
            try {
                CheatingOffense co = CheatingOffense.valueOf(splitted[1]);
                co.setEnabled(!co.isEnabled());
            } catch (IllegalArgumentException iae) {
                mc.dropMessage("Offense " + splitted[1] + " not found");
            }
        } else if (splitted[0].equals("!tdrops")) {
            player.getMap().toggleDrops();
        } else if (splitted[0].equals("!dropd")) {
            if (splitted.length > 1) {
                player.getMap().toggleDrops();
                final ChannelServer cservv = player.getClient().getChannelServer();
                final int mapid = player.getMapId();
                ScheduledFuture<?> scheduleFuture = TimerManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        cservv.getMapFactory().getMap(mapid).toggleDrops();
                    }
                }, Integer.parseInt(splitted[1]) * 1000);
            }
        } else if (splitted[0].equals("!givebuff")) {
            long mask = 0;
            mask |= Long.decode(splitted[1]);
            c.getSession().write(MaplePacketCreator.giveBuffTest(1000, 60, mask));
        } else if (splitted[0].equals("!givemonsbuff")) {
            int mask = 0;
            mask |= Integer.decode(splitted[1]);
            MobSkill skill = MobSkillFactory.getMobSkill(128, 1);
            c.getSession().write(MaplePacketCreator.applyMonsterStatusTest(Integer.valueOf(splitted[2]), mask, 0, skill, Integer.valueOf(splitted[3])));
        } else if (splitted[0].equals("!givemonstatus")) {
            int mask = 0;
            mask |= Integer.decode(splitted[1]);
            c.getSession().write(MaplePacketCreator.applyMonsterStatusTest2(Integer.valueOf(splitted[2]), mask, 1000, Integer.valueOf(splitted[3])));
        } else if (splitted[0].equals("!sreactor")) {
            MapleReactorStats reactorSt = MapleReactorFactory.getReactor(Integer.parseInt(splitted[1]));
            MapleReactor reactor = new MapleReactor(reactorSt, Integer.parseInt(splitted[1]));
            reactor.setDelay(-1);
            reactor.setPosition(c.getPlayer().getPosition());
            c.getPlayer().getMap().spawnReactor(reactor);
        } else if (splitted[0].equals("!hreactor")) {
            c.getPlayer().getMap().getReactorByOid(Integer.parseInt(splitted[1])).hitReactor(c);
        } else if (splitted[0].equals("!lreactor")) {
            MapleMap map = c.getPlayer().getMap();
            List<MapleMapObject> reactors = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
            for (MapleMapObject reactorL : reactors) {
                MapleReactor reactor2l = (MapleReactor) reactorL;
                mc.dropMessage("Reactor: oID: " + reactor2l.getObjectId() + " reactorID: " + reactor2l.getId() + " Position: " + reactor2l.getPosition().toString() + " State: " + reactor2l.getState());
            }
        } else if (splitted[0].equals("!dreactor")) {
            MapleMap map = c.getPlayer().getMap();
            List<MapleMapObject> reactors = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
            if (splitted[1].equals("all")) {
                for (MapleMapObject reactorL : reactors) {
                    MapleReactor reactor2l = (MapleReactor) reactorL;
                    c.getPlayer().getMap().destroyReactor(reactor2l.getObjectId());
                }
            } else {
                c.getPlayer().getMap().destroyReactor(Integer.parseInt(splitted[1]));
            }
        } else if (splitted[0].equals("!rreactor")) {
            c.getPlayer().getMap().resetReactors();
        } else if (splitted[1].equals("!vac")) {
            for (MapleMapObject mmo : c.getPlayer().getMap().getMapObjects()) {
                if (mmo instanceof MapleMonster) {
                    MapleMonster monster = (MapleMonster) mmo;
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.moveMonster(0, -1, 0, 0, 0, monster.getObjectId(), monster.getPosition(), c.getPlayer().getLastRes()));
                    monster.setPosition(c.getPlayer().getPosition());
                }
            }
        }
    }
