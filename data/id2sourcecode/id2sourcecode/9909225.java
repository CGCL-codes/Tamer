    public void damage(MapleCharacter from, int damage, boolean updateAttackTime) {
        AttackerEntry attacker = null;
        if (from.getParty() != null) {
            attacker = new PartyAttackerEntry(from.getParty().getId(), from.getClient().getChannelServer());
        } else {
            attacker = new SingleAttackerEntry(from, from.getClient().getChannelServer());
        }
        boolean replaced = false;
        for (AttackerEntry aentry : attackers) {
            if (aentry.equals(attacker)) {
                attacker = aentry;
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            attackers.add(attacker);
        }
        int rDamage = Math.max(0, Math.min(damage, this.hp));
        attacker.addDamage(from, rDamage, updateAttackTime);
        this.hp -= rDamage;
        int remhppercentage = (int) Math.ceil((this.hp * 100.0) / getMaxHp());
        if (remhppercentage < 1) {
            remhppercentage = 1;
        }
        long okTime = System.currentTimeMillis() - 4000;
        if (hasBossHPBar()) {
            from.getMap().broadcastMessage(makeBossHPBarPacket(), getPosition());
        } else if (!isBoss()) {
            for (AttackerEntry mattacker : attackers) {
                for (AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                    if (cattacker.getAttacker().getMap() == from.getMap()) {
                        if (cattacker.getLastAttackTime() >= okTime) {
                            cattacker.getAttacker().getClient().getSession().write(MaplePacketCreator.showMonsterHP(getObjectId(), remhppercentage));
                        }
                    }
                }
            }
        }
    }
