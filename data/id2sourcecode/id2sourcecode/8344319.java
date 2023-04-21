    public L2ItemInstance createItem(String process, int itemId, long count, L2PcInstance actor, L2Object reference) {
        L2ItemInstance item = new L2ItemInstance(IdFactory.getInstance().getNextId(), itemId);
        if (process.equalsIgnoreCase("loot")) {
            ScheduledFuture<?> itemLootShedule;
            if (reference instanceof L2Attackable && ((L2Attackable) reference).isRaid()) {
                L2Attackable raid = (L2Attackable) reference;
                if (raid.getFirstCommandChannelAttacked() != null && !Config.AUTO_LOOT_RAIDS) {
                    item.setOwnerId(raid.getFirstCommandChannelAttacked().getChannelLeader().getObjectId());
                    itemLootShedule = ThreadPoolManager.getInstance().scheduleGeneral(new ResetOwner(item), Config.LOOT_RAIDS_PRIVILEGE_INTERVAL);
                    item.setItemLootShedule(itemLootShedule);
                }
            } else if (!Config.AUTO_LOOT) {
                item.setOwnerId(actor.getObjectId());
                itemLootShedule = ThreadPoolManager.getInstance().scheduleGeneral(new ResetOwner(item), 15000);
                item.setItemLootShedule(itemLootShedule);
            }
        }
        if (Config.DEBUG) _log.fine("ItemTable: Item created  oid:" + item.getObjectId() + " itemid:" + itemId);
        L2World.getInstance().storeObject(item);
        if (item.isStackable() && count > 1) item.setCount(count);
        if (Config.LOG_ITEMS && !process.equals("Reset")) {
            LogRecord record = new LogRecord(Level.INFO, "CREATE:" + process);
            record.setLoggerName("item");
            record.setParameters(new Object[] { item, actor, reference });
            _logItems.log(record);
        }
        if (actor != null) {
            if (actor.isGM()) {
                String referenceName = "no-reference";
                if (reference != null) {
                    referenceName = (reference.getName() != null ? reference.getName() : "no-name");
                }
                String targetName = (actor.getTarget() != null ? actor.getTarget().getName() : "no-target");
                if (Config.GMAUDIT) GMAudit.auditGMAction(actor.getName() + " [" + actor.getObjectId() + "]", process + "(id: " + itemId + " count: " + count + " name: " + item.getItemName() + " objId: " + item.getObjectId() + ")", targetName, "L2Object referencing this action is: " + referenceName);
            }
        }
        return item;
    }
