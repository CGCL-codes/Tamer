    public static void rewardTeam(String teamName) {
        if (!ItemTable.getInstance().createDummyItem(_rewardId).isStackable() && teamName == null) return;
        if (teamName == null) Announcements(_eventName + "(FOS): The prize will be divided between both teams.");
        int stackableCount = _rewardAmount;
        for (L2PcInstance player : _players) if (player != null) if ((teamName == null || player._teamNameFOS.equals(teamName)) && (player._countFOSKills > 0 || Config.FortressSiege_PRICE_NO_KILLS)) {
            _rewardAmount = stackableCount;
            if (teamName == null && _rewardAmount > 1) _rewardAmount = (_rewardAmount + 1) / 2;
            PcInventory inv = player.getInventory();
            if (ItemTable.getInstance().createDummyItem(_rewardId).isStackable()) inv.addItem("FortressSiege: " + _eventName, _rewardId, _rewardAmount, player, null); else for (int i = 0; i <= _rewardAmount - 1; i++) inv.addItem("FortressSiege: " + _eventName, _rewardId, 1, player, null);
            SystemMessage sm;
            if (_rewardAmount > 1) {
                sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
                sm.addItemName(_rewardId);
                sm.addNumber(_rewardAmount);
                player.sendPacket(sm);
            } else {
                sm = new SystemMessage(SystemMessageId.EARNED_ITEM);
                sm.addItemName(_rewardId);
                player.sendPacket(sm);
            }
            StatusUpdate su = new StatusUpdate(player.getObjectId());
            su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
            player.sendPacket(su);
            NpcHtmlMessage nhm = new NpcHtmlMessage(5);
            TextBuilder replyMSG = new TextBuilder("");
            replyMSG.append("<html><body>Your team did a good job. Look in your inventory for the reward.</body></html>");
            nhm.setHtml(replyMSG.toString());
            player.sendPacket(nhm);
            ItemList il = new ItemList(player, true);
            player.sendPacket(il);
            player.sendPacket(ActionFailed.STATIC_PACKET);
        }
    }
