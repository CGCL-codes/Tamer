    public static CombatArmy findCombatArmyRequiredToCapturePopCenter(PopulationCenterSizeEnum pcSize, FortificationSizeEnum fort, int loyalty, ClimateEnum climate, HexTerrainEnum terrain) {
        if (pcSize.getCode() == 0) return null;
        CombatPopCenter pc = new CombatPopCenter();
        pc.setSize(pcSize);
        pc.setFort(fort);
        pc.setLoyalty(loyalty);
        int nhi1 = 1;
        int nhi2 = 30000;
        while (true) {
            pc.setCaptured(false);
            int nhi = (nhi1 + nhi2) / 2;
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
            combat.addToSide(0, ca2);
            combat.setSide2Pc(pc);
            combat.setAttackPopCenter(true);
            combat.setClimate(climate);
            combat.setTerrain(terrain);
            combat.autoSetRelationsToHated();
            combat.runWholeCombat();
            int pcDefense = combat.computePopCenterStrength(pc);
            int diff = pc.getStrengthOfAttackingArmies() - pcDefense;
            if (Math.abs(diff) < 50) {
                return ca2;
            } else if (diff < 0) {
                nhi1 = nhi;
            } else {
                nhi2 = nhi;
            }
        }
    }
