    private static void monsterBomb(MapleCharacter player, MapleCharacter attackedPlayers, MapleMap map, AbstractDealDamageHandler.AttackInfo attack) {
        if (attackedPlayers.getLevel() > player.getLevel() + 25) {
            pvpDamage *= 1.35;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 25) {
            pvpDamage /= 1.35;
        } else if (attackedPlayers.getLevel() > player.getLevel() + 100) {
            pvpDamage *= 1.50;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 100) {
            pvpDamage /= 1.50;
        }
        if (player.getJob().equals(MapleJob.MAGICIAN)) {
            pvpDamage *= 1.20;
        }
        Integer mguard = attackedPlayers.getBuffedValue(MapleBuffStat.MAGIC_GUARD);
        Integer mesoguard = attackedPlayers.getBuffedValue(MapleBuffStat.MESOGUARD);
        if (mguard != null) {
            int mploss = (int) (pvpDamage / .5);
            pvpDamage *= .70;
            if (mploss > attackedPlayers.getMp()) {
                pvpDamage /= .70;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MAGIC_GUARD);
            } else {
                attackedPlayers.setMp(attackedPlayers.getMp() - mploss);
                attackedPlayers.updateSingleStat(MapleStat.MP, attackedPlayers.getMp());
            }
        } else if (mesoguard != null) {
            int mesoloss = (int) (pvpDamage * .75);
            pvpDamage *= .75;
            if (mesoloss > attackedPlayers.getMeso()) {
                pvpDamage /= .75;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MESOGUARD);
            } else {
                attackedPlayers.gainMeso(-mesoloss, false);
            }
        }
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9400711);
        map.spawnMonsterOnGroundBelow(pvpMob, attackedPlayers.getPosition(), false);
        for (int attacks = 0; attacks < attack.numDamage; attacks++) {
            map.broadcastMessage(MaplePacketCreator.damagePlayer(attack.numDamage, pvpMob.getId(), attackedPlayers.getId(), pvpDamage));
            attackedPlayers.addHP(-pvpDamage);
        }
        int attackedDamage = pvpDamage * attack.numDamage;
        attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(5, player.getName() + " has hit you for " + attackedDamage + " damage!"));
        map.killMonster(pvpMob, player, false);
        if (attackedPlayers.getHp() <= 0 && !attackedPlayers.isAlive()) {
            int expReward = attackedPlayers.getLevel() * 100;
            int gpReward = (int) (Math.floor(Math.random() * (200 - 50) + 50));
            if (player.getPvpKills() * .25 >= player.getPvpDeaths()) {
                expReward *= 20;
            }
            player.gainExp(expReward, true, false);
            if (player.getGuildId() != 0 && player.getGuildId() != attackedPlayers.getGuildId()) {
                try {
                    MapleGuild guild = player.getClient().getChannelServer().getWorldInterface().getGuild(player.getGuildId(), null);
                    guild.gainGP(gpReward);
                } catch (Exception e) {
                }
            }
            player.gainPvpKill();
            player.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You've killed " + attackedPlayers.getName() + "!! You've gained a pvp kill!"));
            attackedPlayers.gainPvpDeath();
            attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " has killed you!"));
        }
    }
