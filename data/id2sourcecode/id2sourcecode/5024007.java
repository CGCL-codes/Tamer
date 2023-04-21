    public synchronized net.sourceforge.huntforgold.model.Ship createShip(int index, int nationality) {
        net.sourceforge.huntforgold.xml.Ship result = (net.sourceforge.huntforgold.xml.Ship) shipMap.get(new Integer(index));
        if (result != null) {
            counter = counter + 1;
            int uniqueId = counter;
            String name = result.getName();
            String pngImage = result.getPngImage();
            int typicalCrew = result.getTypicalCrew();
            int maxCrew = result.getMaxCrew();
            int maxCargo = result.getMaxCargo();
            int maxHitPoints = result.getMaxHitPoints();
            int maxSpeed = result.getMaxSpeed();
            int typicalCannons = result.getTypicalCannons();
            int maxCannons = result.getMaxCannons();
            double turnSpeed = result.getTurnSpeed();
            net.sourceforge.huntforgold.model.Ship ship = new net.sourceforge.huntforgold.model.Ship(uniqueId, name, pngImage, typicalCrew, maxCrew, maxCargo, maxHitPoints, maxSpeed, typicalCannons, maxCannons, turnSpeed, nationality);
            int crew = random.nextInt(maxCrew + 1);
            if (crew < 8) {
                crew = 8;
            }
            ship.setCrew(crew);
            ship.setCaptain("Enemy captain");
            ship.setFood(0);
            ship.setGoods(0);
            ship.setSugar(0);
            int cannons = random.nextInt(maxCannons + 1);
            if (cannons % 2 == 1) {
                cannons -= 1;
            }
            ship.setCannons(cannons);
            int gold = random.nextInt(51) * 20;
            ship.setGold(gold);
            return ship;
        } else {
            return null;
        }
    }
