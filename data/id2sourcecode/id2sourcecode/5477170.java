    private boolean applyTo(MapleCharacter applyfrom, MapleCharacter applyto, boolean primary, Point pos) {
        int hpchange = calcHPChange(applyfrom, primary);
        int mpchange = calcMPChange(applyfrom, primary);
        if (primary) {
            if (itemConNo != 0) {
                MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(itemCon);
                MapleInventoryManipulator.removeById(applyto.getClient(), type, itemCon, itemConNo, false, true);
            }
        }
        List<Pair<MapleStat, Integer>> hpmpupdate = new ArrayList<Pair<MapleStat, Integer>>(2);
        if (!primary && isResurrection()) {
            hpchange = applyto.getMaxHp();
            applyto.setStance(0);
        }
        if (isDispel() && makeChanceResult()) {
            applyto.dispelDebuffs();
        }
        if (isHeroWill()) {
            applyto.dispelSeduce();
        }
        if (!applyto.isPvPMap()) {
            if (hpchange != 0) {
                if (hpchange < 0 && (-hpchange) > applyto.getHp()) {
                    return false;
                }
                int newHp = applyto.getHp() + hpchange;
                if (newHp < 1) {
                    newHp = 1;
                }
                applyto.setHp(newHp);
                hpmpupdate.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(applyto.getHp())));
            }
        }
        if (mpchange != 0) {
            if (mpchange < 0 && (-mpchange) > applyto.getMp()) {
                return false;
            }
            if (!applyto.isGM()) applyto.setMp(applyto.getMp() + mpchange);
            hpmpupdate.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(applyto.getMp())));
        }
        applyto.getClient().getSession().write(MaplePacketCreator.updatePlayerStats(hpmpupdate, true));
        if (moveTo != -1) {
            if (applyto.getMap().getReturnMapId() != applyto.getMapId()) {
                MapleMap target;
                if (moveTo == 999999999) {
                    target = applyto.getMap().getReturnMap();
                } else {
                    target = ChannelServer.getInstance(applyto.getClient().getChannel()).getMapFactory().getMap(moveTo);
                    if (target.getId() / 10000000 != 60 && applyto.getMapId() / 10000000 != 61) {
                        if (target.getId() / 10000000 != 21 && applyto.getMapId() / 10000000 != 20) {
                            if (target.getId() / 10000000 != applyto.getMapId() / 10000000) {
                                return false;
                            }
                        }
                    }
                }
                applyto.changeMap(target, target.getPortal(0));
            } else {
                return false;
            }
        }
        if (isShadowClaw()) {
            int projectile = 0;
            MapleInventory use = applyto.getInventory(MapleInventoryType.USE);
            MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
            for (int i = 0; i < 255; i++) {
                IItem item = use.getItem((byte) i);
                if (item != null) {
                    boolean isStar = mii.isThrowingStar(item.getItemId());
                    if (isStar && item.getQuantity() >= 200) {
                        projectile = item.getItemId();
                        break;
                    }
                }
            }
            if (projectile == 0) {
                return false;
            } else {
                MapleInventoryManipulator.removeById(applyto.getClient(), MapleInventoryType.USE, projectile, 200, false, true);
            }
        }
        if (overTime) {
            applyBuffEffect(applyfrom, applyto, primary);
        }
        if (primary && (overTime || isHeal())) {
            applyBuff(applyfrom);
        }
        if (primary && isMonsterBuff()) {
            applyMonsterBuff(applyfrom);
        }
        SummonMovementType summonMovementType = getSummonMovementType();
        if (summonMovementType != null && pos != null) {
            final MapleSummon tosummon = new MapleSummon(applyfrom, sourceid, pos, summonMovementType);
            if (!tosummon.isPuppet()) {
                applyfrom.getCheatTracker().resetSummonAttack();
            }
            applyfrom.getMap().spawnSummon(tosummon);
            applyfrom.getSummons().put(sourceid, tosummon);
            tosummon.addHP(x);
            if (isBeholder()) {
                tosummon.addHP(1);
            }
        }
        if (isMagicDoor()) {
            Point doorPosition = new Point(applyto.getPosition());
            MapleDoor door = new MapleDoor(applyto, doorPosition);
            applyto.getMap().spawnDoor(door);
            applyto.addDoor(door);
            door = new MapleDoor(door);
            applyto.addDoor(door);
            door.getTown().spawnDoor(door);
            if (applyto.getParty() != null) {
                applyto.silentPartyUpdate();
            }
            applyto.disableDoor();
        } else if (isMist()) {
            Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
            MapleMist mist = new MapleMist(bounds, applyfrom, this);
            applyfrom.getMap().spawnMist(mist, getDuration(), sourceid == 2111003, false);
        } else if (isTimeLeap()) {
            for (PlayerCoolDownValueHolder i : applyto.getAllCooldowns()) {
                if (i.skillId != 5121010) {
                    applyto.removeCooldown(i.skillId);
                }
            }
        }
        return true;
    }
