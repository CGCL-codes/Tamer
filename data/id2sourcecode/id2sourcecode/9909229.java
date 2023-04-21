    public MapleCharacter killBy(MapleCharacter killer) {
        long totalBaseExpL = this.getExp() * ChannelServer.getInstance(killer.getClient().getChannel()).getExpRate() * killer.getClient().getPlayer().hasEXPCard() * killer.getClient().GetEXPModifier() * (int) Math.sqrt(killer.getLevel());
        int totalBaseExp = (int) (Math.min(Integer.MAX_VALUE, totalBaseExpL));
        AttackerEntry highest = null;
        int highdamage = 0;
        for (AttackerEntry attackEntry : attackers) {
            if (attackEntry.getDamage() > highdamage) {
                highest = attackEntry;
                highdamage = attackEntry.getDamage();
            }
        }
        for (AttackerEntry attackEntry : attackers) {
            int baseExp = (int) Math.ceil(totalBaseExp * ((double) attackEntry.getDamage() / getMaxHp()));
            attackEntry.killedMob(killer.getMap(), baseExp, attackEntry == highest);
        }
        if (this.getController() != null) {
            getController().getClient().getSession().write(MaplePacketCreator.stopControllingMonster(this.getObjectId()));
            getController().stopControllingMonster(this);
        }
        final List<Integer> toSpawn = this.getRevives();
        if (toSpawn != null) {
            final MapleMap reviveMap = killer.getMap();
            TimerManager.getInstance().schedule(new Runnable() {

                public void run() {
                    for (Integer mid : toSpawn) {
                        MapleMonster mob = MapleLifeFactory.getMonster(mid);
                        if (eventInstance != null) {
                            eventInstance.registerMonster(mob);
                        }
                        mob.setPosition(getPosition());
                        if (dropsDisabled()) {
                            mob.disableDrops();
                        }
                        reviveMap.spawnRevives(mob);
                    }
                }
            }, this.getAnimationTime("die1"));
        }
        if (eventInstance != null) {
            eventInstance.unregisterMonster(this);
        }
        for (MonsterListener listener : listeners.toArray(new MonsterListener[listeners.size()])) {
            listener.monsterKilled(this, highestDamageChar);
        }
        MapleCharacter ret = highestDamageChar;
        highestDamageChar = null;
        return ret;
    }
