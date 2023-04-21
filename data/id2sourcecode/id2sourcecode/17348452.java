    public static int getNakedHeavyInfantryEquivalent3(CombatArmy ca) {
        int nhi1 = getNakedHeavyInfantryEquivalent(ca) * 4 / 3;
        int nhi2 = getNakedHeavyInfantryEquivalent(ca) / 4 * 3;
        while (true) {
            int nhi = (nhi1 + nhi2) / 2;
            ca.setLosses(0);
            CombatArmy ca2 = new CombatArmy();
            ArrayList<ArmyElement> aes = new ArrayList<ArmyElement>();
            ArmyElement ae = new ArmyElement(ArmyElementType.HeavyInfantry, nhi);
            ae.setTraining(10);
            ae.setWeapons(10);
            ae.setArmor(0);
            aes.add(ae);
            ca2.setElements(aes);
            ca2.setMorale(30);
            ca2.setCommandRank(30);
            Combat combat = new Combat();
            combat.setMaxRounds(20);
            combat.addToSide(0, ca);
            combat.addToSide(1, ca2);
            combat.setClimate(ClimateEnum.Mild);
            combat.setTerrain(HexTerrainEnum.plains);
            combat.runArmyBattle();
            if ((ca.getLosses() > 98 && ca2.getLosses() > 98) || Math.abs(ca2.getLosses() - ca.getLosses()) < 1) {
                return nhi;
            } else if (nhi1 - nhi2 < 2) {
                return nhi;
            } else if (ca.getLosses() > ca2.getLosses()) {
                nhi1 = nhi;
            } else {
                nhi2 = nhi;
            }
        }
    }
