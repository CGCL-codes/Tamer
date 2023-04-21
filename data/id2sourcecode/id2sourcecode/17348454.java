    public static int canCapturePopCenter(Army a, PopulationCenterSizeEnum pcSize, FortificationSizeEnum fort) {
        if (pcSize.getCode() == 0) return -1;
        PopulationCenter pc = new PopulationCenter();
        pc.setSize(pcSize);
        pc.setFortification(fort);
        int l1 = 100;
        int l2 = 0;
        int lastCaptureLoyalty = -1;
        do {
            int l = (l1 + l2) / 2;
            CombatArmy ca = new CombatArmy(a);
            CombatPopCenter cpc = new CombatPopCenter(pc);
            cpc.setLoyalty(l);
            Combat combat = new Combat();
            combat.addToSide(0, ca);
            combat.setSide2Pc(cpc);
            combat.getSide1Relations()[0][10] = NationRelationsEnum.Hated;
            combat.setMaxRounds(10);
            combat.setClimate(ClimateEnum.Mild);
            combat.setTerrain(HexTerrainEnum.plains);
            combat.runPcBattle(0, 0);
            if (cpc.isCaptured()) {
                lastCaptureLoyalty = l;
                if (l2 < l) {
                    l2 = l;
                } else {
                    l2++;
                    if (l1 < l2) break;
                }
            } else {
                if (l1 > l) {
                    l1 = l;
                } else {
                    l1--;
                    if (l1 < l2) break;
                }
            }
        } while (true);
        return lastCaptureLoyalty;
    }
