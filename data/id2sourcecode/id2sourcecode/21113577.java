    public void run(MapleCharacter c, Integer extSelection) {
        MapleQuestStatus status;
        ServernoticeMapleClientMessageCallback snmcmc = new ServernoticeMapleClientMessageCallback(5, c.getClient());
        switch(type) {
            case EXP:
                status = c.getQuest(quest);
                if (status.getStatus() == MapleQuestStatus.Status.NOT_STARTED && status.getForfeited() > 0) {
                    break;
                }
                c.gainExp(MapleDataTool.getInt(data) * ChannelServer.getInstance(c.getClient().getChannel()).getExpRate(), true, true);
                break;
            case ITEM:
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                Map<Integer, Integer> props = new HashMap<Integer, Integer>();
                for (MapleData iEntry : data.getChildren()) {
                    if (iEntry.getChildByPath("prop") != null && MapleDataTool.getInt(iEntry.getChildByPath("prop")) != -1 && canGetItem(iEntry, c)) {
                        for (int i = 0; i < MapleDataTool.getInt(iEntry.getChildByPath("prop")); i++) {
                            props.put(props.size(), MapleDataTool.getInt(iEntry.getChildByPath("id")));
                        }
                    }
                }
                int selection = 0;
                int extNum = 0;
                if (props.size() > 0) {
                    Random r = new Random();
                    selection = props.get(r.nextInt(props.size()));
                }
                for (MapleData iEntry : data.getChildren()) {
                    if (!canGetItem(iEntry, c)) {
                        continue;
                    }
                    if (iEntry.getChildByPath("prop") != null) {
                        if (MapleDataTool.getInt(iEntry.getChildByPath("prop")) == -1) {
                            if (extSelection != extNum++) {
                                continue;
                            }
                        } else if (MapleDataTool.getInt(iEntry.getChildByPath("id")) != selection) {
                            continue;
                        }
                    }
                    if (MapleDataTool.getInt(iEntry.getChildByPath("count")) < 0) {
                        int itemId = MapleDataTool.getInt(iEntry.getChildByPath("id"));
                        MapleInventoryType iType = ii.getInventoryType(itemId);
                        short quantity = (short) (MapleDataTool.getInt(iEntry.getChildByPath("count")) * -1);
                        try {
                            MapleInventoryManipulator.removeById(c.getClient(), iType, itemId, quantity, true, false);
                        } catch (InventoryException ie) {
                            log.warn("[h4x] Completing a quest without meeting the requirements", ie);
                        }
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(itemId, (short) MapleDataTool.getInt(iEntry.getChildByPath("count")), true));
                    } else {
                        int itemId = MapleDataTool.getInt(iEntry.getChildByPath("id"));
                        short quantity = (short) MapleDataTool.getInt(iEntry.getChildByPath("count"));
                        StringBuilder logInfo = new StringBuilder(c.getName());
                        logInfo.append(" received ");
                        logInfo.append(quantity);
                        logInfo.append(" as reward from a quest");
                        MapleInventoryManipulator.addById(c.getClient(), itemId, quantity, logInfo.toString());
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(itemId, quantity, true));
                    }
                }
                break;
            case MESO:
                status = c.getQuest(quest);
                if (status.getStatus() == MapleQuestStatus.Status.NOT_STARTED && status.getForfeited() > 0) {
                    break;
                }
                c.gainMeso(MapleDataTool.getInt(data) * ChannelServer.getInstance(c.getClient().getChannel()).getMesoRate(), true, false, true);
                break;
            case QUEST:
                for (MapleData qEntry : data) {
                    int questid = MapleDataTool.getInt(qEntry.getChildByPath("id"));
                    int stat = MapleDataTool.getInt(qEntry.getChildByPath("state"));
                    c.updateQuest(new MapleQuestStatus(MapleQuest.getInstance(questid), MapleQuestStatus.Status.getById(stat)));
                }
                break;
            case SKILL:
                for (MapleData sEntry : data) {
                    int skillid = MapleDataTool.getInt(sEntry.getChildByPath("id"));
                    int skillLevel = MapleDataTool.getInt(sEntry.getChildByPath("skillLevel"));
                    int masterLevel = MapleDataTool.getInt(sEntry.getChildByPath("masterLevel"));
                    ISkill skillObject = SkillFactory.getSkill(skillid);
                    boolean shouldLearn = false;
                    MapleData applicableJobs = sEntry.getChildByPath("job");
                    for (MapleData applicableJob : applicableJobs) {
                        MapleJob job = MapleJob.getById(MapleDataTool.getInt(applicableJob));
                        if (c.getJob() == job) {
                            shouldLearn = true;
                            break;
                        }
                    }
                    if (skillObject.isBeginnerSkill()) {
                        shouldLearn = true;
                    }
                    skillLevel = Math.max(skillLevel, c.getSkillLevel(skillObject));
                    masterLevel = Math.max(masterLevel, c.getMasterLevel(skillObject));
                    if (shouldLearn) {
                        c.changeSkillLevel(skillObject, skillLevel, masterLevel);
                        snmcmc.dropMessage("You have learned " + SkillFactory.getSkillName(skillid) + " with level " + skillLevel + " and with max level " + masterLevel);
                    }
                }
                break;
            case FAME:
                status = c.getQuest(quest);
                if (status.getStatus() == MapleQuestStatus.Status.NOT_STARTED && status.getForfeited() > 0) {
                    break;
                }
                c.addFame(MapleDataTool.getInt(data));
                c.updateSingleStat(MapleStat.FAME, c.getFame());
                int fameGain = MapleDataTool.getInt(data);
                c.getClient().getSession().write(MaplePacketCreator.getShowFameGain(fameGain));
                break;
            case BUFF:
                status = c.getQuest(quest);
                if (status.getStatus() == MapleQuestStatus.Status.NOT_STARTED && status.getForfeited() > 0) {
                    break;
                }
                MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
                mii.getItemEffect(MapleDataTool.getInt(data)).applyTo(c);
                break;
            default:
        }
    }
