    private IndianSettlement placeIndianSettlement(Player player, boolean capital, Position position, Map map) {
        final Tile tile = map.getTile(position);
        String name = (capital) ? player.getCapitalName() : player.getSettlementName();
        if (Player.ASSIGN_SETTLEMENT_NAME.equals(name)) {
            player.installSettlementNames(Messages.getSettlementNames(player), random);
            name = (capital) ? player.getCapitalName() : player.getSettlementName();
        }
        UnitType skill = generateSkillForLocation(map, tile, player.getNationType());
        IndianSettlement settlement = new ServerIndianSettlement(map.getGame(), player, name, tile, capital, skill, new HashSet<Player>(), null);
        player.addSettlement(settlement);
        logger.fine("Generated skill: " + settlement.getLearnableSkill());
        int low = settlement.getType().getMinimumSize();
        int high = settlement.getType().getMaximumSize();
        int unitCount = low + random.nextInt(high - low);
        for (int i = 0; i < unitCount; i++) {
            UnitType unitType = map.getSpecification().getUnitType("model.unit.brave");
            Unit unit = new ServerUnit(map.getGame(), settlement, player, unitType, unitType.getDefaultEquipment());
            unit.setIndianSettlement(settlement);
            if (i == 0) {
                unit.setLocation(tile);
            } else {
                unit.setLocation(settlement);
            }
        }
        settlement.placeSettlement(true);
        if (FreeCol.isInDebugMode()) {
            for (GoodsType goodsType : map.getSpecification().getGoodsTypeList()) {
                if (goodsType.isNewWorldGoodsType()) settlement.addGoods(goodsType, 150);
            }
        }
        return settlement;
    }
