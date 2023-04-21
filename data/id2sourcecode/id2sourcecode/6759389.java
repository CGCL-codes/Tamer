    public void save(ObjectOutputStream oos) {
        try {
            oos.writeObject(row);
            oos.writeObject(col);
            oos.writeObject(index);
            oos.writeObject(playerName);
            oos.writeObject(hitPoints);
            oos.writeObject(maxHitPoints);
            oos.writeObject(fightingAbility);
            oos.writeObject(marksmanAbility);
            oos.writeObject(martialArtsAbility);
            oos.writeObject(thievingAbility);
            oos.writeObject(money);
            oos.writeObject(facing);
            oos.writeObject(availableArmor);
            oos.writeObject(weaponLevels);
            oos.writeObject(numBandaids);
            oos.writeObject(numGrenades);
            oos.writeObject(numDynamite);
            oos.writeObject(numBullets);
            oos.writeObject(numRockets);
            oos.writeObject(numFlamePacks);
            oos.writeObject(numLaddersUp);
            oos.writeObject(numLaddersDown);
            oos.writeObject(numMapViewers);
            oos.writeObject(numExports);
            oos.writeObject(readiedWeaponIndex);
            oos.writeObject(readiedArmorIndex);
            oos.writeObject(caughtStealing);
            oos.writeObject(isDead);
            oos.writeObject(inDungeon);
            oos.writeObject(dungeonLevel);
            oos.writeObject(dungeonPath);
            oos.writeObject(vehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
