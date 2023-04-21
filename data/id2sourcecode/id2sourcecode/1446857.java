    public double calcSpeed(Bot b) {
        int punchPower = 5;
        int kickPower = 5;
        int punchReach = 5;
        int kickReach = 5;
        for (Characteristic c : b.getCharacteristics()) {
            if (c.getCharacterName().equalsIgnoreCase("punchPower")) {
                punchPower = c.getCharacterValue();
            }
            ;
            if (c.getCharacterName().equalsIgnoreCase("kickPower")) {
                kickPower = c.getCharacterValue();
            }
            ;
            if (c.getCharacterName().equalsIgnoreCase("punchReach")) {
                punchReach = c.getCharacterValue();
            }
            ;
            if (c.getCharacterName().equalsIgnoreCase("kickReach")) {
                kickReach = c.getCharacterValue();
            }
            ;
        }
        double weight = (punchPower + kickPower) / 2;
        double heigth = (punchReach + kickReach) / 2;
        double speed = Math.abs(0.5 * (heigth - weight));
        if (speed == 0) {
            speed = 1.0;
        }
        return speed;
    }
