    public static void battle(String battleName, Expedition attacker, Actor defender) {
        List<Equipment> attackingUnitsFullGroup = attacker.getGoods(GoodType.PEOPLE);
        List<Equipment> defendingUnitsFullGroup = null;
        int attackingMoraleModifier = attacker.getMoraleAttackModifier();
        if (Util.chance(20)) attackingMoraleModifier = 0;
        int defendingMoraleModifier = 0;
        if (defender instanceof NativeTown) {
            NativeTown town = (NativeTown) defender;
            if (town.getTotalUnits() == 0) {
                if (attacker == ExpeditionGame.getCurrentGame().getPlayer()) {
                    ((ExpeditionUserInterface) UserInterface.getUI()).transferFromCache("Select the goods to plunder", null, town);
                }
                return;
            } else {
                defendingUnitsFullGroup = town.getGoods(GoodType.PEOPLE);
                town.resetTurnsBeforeNextExpedition();
                defender.getLevel().getDispatcher().removeActor(town);
                defender.getLevel().getDispatcher().addActor(town, true);
            }
        } else if (defender instanceof Expedition) {
            Expedition npe = (Expedition) defender;
            defendingUnitsFullGroup = npe.getGoods(GoodType.PEOPLE);
            defendingMoraleModifier = npe.getMoraleAttackModifier();
            if (Util.chance(20)) defendingMoraleModifier = 0;
        } else {
            return;
        }
        defender.setInterrupted();
        List<Equipment> attackingUnits = selectSquad(cloneEquipmentList(attackingUnitsFullGroup));
        List<Equipment> defendingUnits = selectSquad(cloneEquipmentList(defendingUnitsFullGroup));
        List<Equipment> originalAttackingUnits = cloneEquipmentList(attackingUnits);
        List<Equipment> originalDefendingUnits = cloneEquipmentList(defendingUnits);
        ((ExpeditionUserInterface) UserInterface.getUI()).showBattleScene(battleName, attackingUnits, defendingUnits);
        AssaultOutcome attackerRangedAttackOutcome = rangedAttack(attackingUnits, attackingMoraleModifier, defendingUnits, defendingMoraleModifier, (UnitContainer) defender);
        AssaultOutcome defenderRangedAttackOutcome = rangedAttack(defendingUnits, defendingMoraleModifier, attackingUnits, attackingMoraleModifier, attacker);
        Pair<AssaultOutcome, AssaultOutcome> attackerMountedAttackOutcome = mountedAttack(attackingUnits, attackingMoraleModifier, attacker, defendingUnits, defendingMoraleModifier, (UnitContainer) defender);
        Pair<AssaultOutcome, AssaultOutcome> attackerMeleeAttackOutcome = meleeAttack(attackingUnits, attackingMoraleModifier, attacker, defendingUnits, defendingMoraleModifier, (UnitContainer) defender);
        int attackerScore = 0;
        int defenderScore = 0;
        attackerScore += eval(attackerRangedAttackOutcome);
        defenderScore += eval(defenderRangedAttackOutcome);
        attackerScore += eval(attackerMountedAttackOutcome.getA());
        defenderScore += eval(attackerMountedAttackOutcome.getB());
        attackerScore += eval(attackerMeleeAttackOutcome.getA());
        defenderScore += eval(attackerMeleeAttackOutcome.getB());
        if (attackerScore > defenderScore) {
            attacker.increaseWinBalance();
            if (defender instanceof NativeTown) {
                ((NativeTown) defender).increaseScaredLevel();
            } else if (defender instanceof Expedition) {
                ((Expedition) defender).decreaseWinBalance();
            }
        } else {
            attacker.decreaseWinBalance();
            if (defender instanceof NativeTown) {
                ((NativeTown) defender).reduceScaredLevel();
            } else if (defender instanceof Expedition) {
                ((Expedition) defender).increaseWinBalance();
            }
        }
        ((ExpeditionUserInterface) UserInterface.getUI()).showBattleResults(originalAttackingUnits, originalDefendingUnits, battleName, attackerRangedAttackOutcome, defenderRangedAttackOutcome, attackerMountedAttackOutcome, attackerMeleeAttackOutcome, attackerScore, defenderScore);
        if (attacker != ExpeditionGame.getCurrentGame().getPlayer()) {
            attacker.checkDeath();
        }
        if (defender != ExpeditionGame.getCurrentGame().getPlayer()) {
            if (defender instanceof Expedition) {
                ((Expedition) defender).checkDeath();
            }
        }
    }
