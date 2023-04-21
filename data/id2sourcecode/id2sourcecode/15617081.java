    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        if (splitted.length < 2) {
            mc.dropMessage("Syntax: !goto <mapname>");
        } else {
            HashMap<String, Integer> gotomaps = new HashMap<String, Integer>();
            gotomaps.put("gmmap", 180000000);
            gotomaps.put("southperry", 60000);
            gotomaps.put("amherst", 1010000);
            gotomaps.put("henesys", 100000000);
            gotomaps.put("ellinia", 101000000);
            gotomaps.put("perion", 102000000);
            gotomaps.put("kerningcity", 103000000);
            gotomaps.put("lithharbour", 104000000);
            gotomaps.put("sleepywood", 105040300);
            gotomaps.put("florinabeach", 110000000);
            gotomaps.put("orbis", 200000000);
            gotomaps.put("happyville", 209000000);
            gotomaps.put("elnath", 211000000);
            gotomaps.put("ludibrium", 220000000);
            gotomaps.put("aquaroad", 230000000);
            gotomaps.put("leafre", 240000000);
            gotomaps.put("mulung", 250000000);
            gotomaps.put("herbtown", 251000000);
            gotomaps.put("omegasector", 221000000);
            gotomaps.put("koreanfolktown", 222000000);
            gotomaps.put("newleafcity", 600000000);
            gotomaps.put("sharenian", 990000000);
            gotomaps.put("pianus", 230040420);
            gotomaps.put("horntail", 240060200);
            gotomaps.put("mushmom", 100000005);
            gotomaps.put("griffey", 240020101);
            gotomaps.put("manon", 240020401);
            gotomaps.put("jrbalrog", 105090900);
            gotomaps.put("zakum", 280030000);
            gotomaps.put("papulatus", 220080001);
            gotomaps.put("showatown", 801000000);
            gotomaps.put("guildhq", 200000301);
            gotomaps.put("mushroomshrine", 800000000);
            gotomaps.put("freemarket", 910000000);
            gotomaps.put("ariant", 260000100);
            gotomaps.put("nautilusport", 120000000);
            gotomaps.put("singapore", 540000000);
            gotomaps.put("amoria", 680000000);
            gotomaps.put("zombiemushmom", 105070002);
            if (gotomaps.containsKey(splitted[1])) {
                MapleMap target = c.getChannelServer().getMapFactory().getMap(gotomaps.get(splitted[1]));
                MaplePortal targetPortal = target.getPortal(0);
                MapleCharacter player = c.getPlayer();
                player.changeMap(target, targetPortal);
            } else {
                mc.dropMessage("Use !goto <location>. Locations are as follows:");
                mc.dropMessage("gmmap, henesys, ellinia, perion, kerningcity, lithharbor, sleepywood, koreanfolktown,");
                mc.dropMessage("orbis, elnath, ludibrium, aquaroad, leafre, mulung, herbtown, omegasector, amoria,");
                mc.dropMessage("horntail, mushmom, jrbalrog, zakum, papulatus, showatown, guildhq, griffey, manon,");
                mc.dropMessage("newleafcity, pianus, mushroomshrine, freemarket, ariant, florinabeach, nautilusport.");
                mc.dropMessage("happyville, singapore, sharenian, zombiemushmom.");
            }
        }
    }
