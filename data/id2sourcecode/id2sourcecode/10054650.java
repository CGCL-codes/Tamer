    public Personality(List<Strength> strenghts) {
        this.strenghts = strenghts;
        int punchPower = 5;
        int kickPower = 5;
        int punchReach = 5;
        int kickReach = 5;
        for (Strength strength : strenghts) {
            if (strength.getCharacteristic().equals(settings.PUNCH_POWER)) punchPower = strength.getValue();
            if (strength.getCharacteristic().equals(settings.KICK_POWER)) kickPower = strength.getValue();
            if (strength.getCharacteristic().equals(settings.PUNCH_REACH)) punchReach = strength.getValue();
            if (strength.getCharacteristic().equals(settings.KICK_REACH)) kickReach = strength.getValue();
        }
        int weight = (punchPower + kickPower) / 2;
        int height = (punchReach + kickReach) / 2;
        this.speed = (height - weight) / 2;
    }
