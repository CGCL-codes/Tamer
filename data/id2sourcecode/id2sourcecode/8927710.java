    @SuppressWarnings("static-access")
    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception, IllegalCommandSyntaxException {
        MapleCharacter player = c.getPlayer();
        if (splitted[0].equals("!lowhp")) {
            player.setHp(1);
            player.setMp(500);
            player.updateSingleStat(MapleStat.HP, 1);
            player.updateSingleStat(MapleStat.MP, 500);
        } else if (splitted[0].equals("!fullhp")) {
            player.setHp(player.getMaxHp());
            player.setMp(player.getMaxMp());
            player.updateSingleStat(MapleStat.HP, player.getMaxHp());
            player.updateSingleStat(MapleStat.MP, player.getMaxMp());
        } else if (splitted[0].equals("!skill")) {
            ISkill skill = SkillFactory.getSkill(Integer.parseInt(splitted[1]));
            int level = getOptionalIntArg(splitted, 2, 1);
            int masterlevel = getOptionalIntArg(splitted, 3, 1);
            if (level > skill.getMaxLevel()) {
                level = skill.getMaxLevel();
            }
            if (masterlevel > skill.getMaxLevel() && skill.isFourthJob()) {
                masterlevel = skill.getMaxLevel();
            } else {
                masterlevel = 0;
            }
            player.changeSkillLevel(skill, level, masterlevel);
        } else if (splitted[0].equals("!sp")) {
            int sp = Integer.parseInt(splitted[1]);
            if (sp + player.getRemainingSp() > Short.MAX_VALUE) {
                sp = Short.MAX_VALUE;
            }
            player.setRemainingSp(sp);
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
        } else if (splitted[0].equals("!ap")) {
            int ap = Integer.parseInt(splitted[1]);
            if (ap + player.getRemainingAp() > Short.MAX_VALUE) {
                ap = Short.MAX_VALUE;
            }
            player.setRemainingAp(ap);
            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
        } else if (splitted[0].equals("!job")) {
            int jobId = Integer.parseInt(splitted[1]);
            if (MapleJob.getById(jobId) != null) {
                player.changeJob(MapleJob.getById(jobId));
            }
        } else if (splitted[0].equals("!whereami")) {
            new ServernoticeMapleClientMessageCallback(c).dropMessage("You are on map " + player.getMap().getId());
        } else if (splitted[0].equals("!shop")) {
            MapleShopFactory sfact = MapleShopFactory.getInstance();
            int shopId = Integer.parseInt(splitted[1]);
            if (sfact.getShop(shopId) != null) {
                MapleShop shop = sfact.getShop(shopId);
                shop.sendShop(c);
            }
        } else if (splitted[0].equals("!meso")) {
            player.gainMeso(Integer.MAX_VALUE - player.getMeso(), true);
        } else if (splitted[0].equals("!levelup")) {
            if (player.getLevel() < 200) {
                player.levelUp();
                player.setExp(0);
            } else {
                mc.dropMessage("You are already level 200!");
            }
        } else if (splitted[0].equals("!item")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            short quantity = (short) getOptionalIntArg(splitted, 2, 1);
            if (Integer.parseInt(splitted[1]) >= 5000000 && Integer.parseInt(splitted[1]) <= 5000100) {
                if (quantity > 1) {
                    quantity = 1;
                }
                int petId = MaplePet.createPet(Integer.parseInt(splitted[1]));
                MapleInventoryManipulator.addById(c, Integer.parseInt(splitted[1]), quantity, player.getName() + "used !item with quantity " + quantity, player.getName(), petId);
                return;
            } else if (ii.isRechargable(Integer.parseInt(splitted[1]))) {
                quantity = (short) ii.getSlotMax(c, Integer.parseInt(splitted[1]));
                MapleInventoryManipulator.addById(c, Integer.parseInt(splitted[1]), quantity, "Rechargable item created.", player.getName(), -1);
                return;
            }
            MapleInventoryManipulator.addById(c, Integer.parseInt(splitted[1]), quantity, player.getName() + "used !item with quantity " + quantity, player.getName(), -1);
        } else if (splitted[0].equals("!ring")) {
            int itemId = Integer.parseInt(splitted[1]);
            String partnerName = splitted[2];
            int partnerId = MapleCharacter.getIdByName(partnerName, 0);
            int[] ret = MapleRing.createRing(c, itemId, player.getId(), player.getName(), partnerId, partnerName);
            if (ret[0] == -1 || ret[1] == -1) {
                mc.dropMessage("There was an unknown error.");
                mc.dropMessage("Make sure the person you are attempting to create a ring with is online.");
            }
        } else if (splitted[0].equals("!drop")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int itemId = Integer.parseInt(splitted[1]);
            short quantity = (short) (short) getOptionalIntArg(splitted, 2, 1);
            IItem toDrop;
            if (ii.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                toDrop = ii.getEquipById(itemId);
            } else {
                toDrop = new Item(itemId, (byte) 0, (short) quantity);
            }
            toDrop.log("Created by " + player.getName() + " using !drop. Quantity: " + quantity, false);
            toDrop.setOwner(player.getName());
            player.getMap().spawnItemDrop(player, player, toDrop, player.getPosition(), true, true);
        } else if (splitted[0].equals("!level")) {
            int quantity = Integer.parseInt(splitted[1]);
            c.getPlayer().setLevel(quantity);
            c.getPlayer().levelUp();
            int newexp = c.getPlayer().getExp();
            if (newexp < 0) {
                c.getPlayer().gainExp(-newexp, false, false);
            }
        } else if (splitted[0].equals("!maxlevel")) {
            player.setExp(0);
            while (player.getLevel() < 200) {
                player.levelUp();
            }
        } else if (splitted[0].equals("!online")) {
            mc.dropMessage("Characters connected to channel " + c.getChannel() + ":");
            Collection<MapleCharacter> chrs = c.getChannelServer().getInstance(c.getChannel()).getPlayerStorage().getAllCharacters();
            for (MapleCharacter chr : chrs) {
                mc.dropMessage(chr.getName() + " at map ID: " + chr.getMapId());
            }
            mc.dropMessage("Total characters on channel " + c.getChannel() + ": " + chrs.size());
        } else if (splitted[0].equals("!saveall")) {
            Collection<ChannelServer> cservs = ChannelServer.getAllInstances();
            for (ChannelServer cserv : cservs) {
                mc.dropMessage("Saving all characters in channel " + cserv.getChannel() + "...");
                Collection<MapleCharacter> chrs = cserv.getPlayerStorage().getAllCharacters();
                for (MapleCharacter chr : chrs) {
                    chr.saveToDB(true);
                }
            }
            mc.dropMessage("All characters saved.");
        } else if (splitted[0].equals("!ariantpq")) {
            if (splitted.length < 2) {
                player.getMap().AriantPQStart();
            } else {
                c.getSession().write(MaplePacketCreator.updateAriantPQRanking(splitted[1], 5, false));
            }
        } else if (splitted[0].equals("!scoreboard")) {
            player.getMap().broadcastMessage(MaplePacketCreator.showAriantScoreBoard());
        }
    }
